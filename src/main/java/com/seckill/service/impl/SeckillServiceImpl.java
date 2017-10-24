package com.seckill.service.impl;


import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessSeckilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessSeckilled;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessSeckilledDao successSeckilledDao;
    @Autowired
    private RedisDao redisDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String slat = "sdwqidJIDODJAQsdq,wpdokaq-=0e1o2ea,d[pad,qd,askd";
    public List<Seckill> getSeckillList() {
        return seckillDao.querryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.querryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            seckill = seckillDao.querryById(seckillId);
            if(seckill==null){
                return new Exposer(false,seckillId);
            }else {
                redisDao.putSeckill(seckill);
            }
        }
        long startTime = seckill.getStartTime().getTime();
        long endTime = seckill.getEndTime().getTime();
        long nowTime = new Date().getTime();
        String md5 = getMD5(seckillId);
        if(seckill==null){
            return new Exposer(false,seckillId);
        }
        if(nowTime<startTime||nowTime>endTime){
            return new Exposer(false,seckillId,nowTime,startTime,endTime);
        }
        return new Exposer(true,md5,seckillId);
    }
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws       SeckillException, RepeatKillException, SeckillCloseException {
        Date nowTime = new Date();
        if(md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        try {
            int insertCount = successSeckilledDao.insertSuccessKilled(seckillId,userPhone);
            if(insertCount<=0){
                throw new RepeatKillException("seckill repeated");
            }else{
                int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
                if(updateCount<=0){
                    throw new SeckillCloseException("seckill is close");
                }else{
                    successSeckilledDao.updateState(SeckillStateEnum.SUCCESS.getState(),seckillId);
                    SuccessSeckilled successSeckilled = successSeckilledDao.querryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successSeckilled);
                }
            }
        }catch (RepeatKillException e1){
            throw e1;
        }catch (SeckillCloseException e2){
            throw e2;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }

   public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
       if(md5==null||!md5.equals(getMD5(seckillId))){
           return new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
       }
       Date killTime = new Date();
       Map<String,Object> map = new HashMap<String, Object>();
       map.put("seckillId",seckillId);
       map.put("phone",userPhone);
       map.put("killTime",killTime);
       map.put("result",null);
       try {
           seckillDao.killByProcedure(map);
           int result =  MapUtils.getInteger(map,"result",-2);
           if(result == 1){
               SuccessSeckilled sk = successSeckilledDao.querryByIdWithSeckill(seckillId,userPhone);
               return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
           }else {
               return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
           }
       }catch (Exception e){
           logger.error(e.getMessage(),e);
           return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
       }
   }
    private String getMD5(long seckillId){
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
