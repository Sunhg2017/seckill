package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            String md5 = exposer.getMd5();
            long phone = 1221383L;
            try{
                SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,md5);
                logger.info("seckillExecution={}",seckillExecution);
            }catch (RepeatKillException e1){
                logger.error(e1.getMessage());
            }catch (SeckillCloseException e2){
                logger.error(e2.getMessage());
            }
        }else {
            logger.warn("exposer={}",exposer);
        }

    }

    @Test
    public void executeSeckill() throws Exception {
        String md5 = "19dd4c88bb10250efc147c7b0a5cc234";
        long id = 1000;
        long phone = 1221353;
        try{
            SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,md5);
            logger.info("seckillExecution={}",seckillExecution);
        }catch (RepeatKillException e1){
            logger.error(e1.getMessage());
        }catch (SeckillCloseException e2){
            logger.error(e2.getMessage());
        }

    }

}