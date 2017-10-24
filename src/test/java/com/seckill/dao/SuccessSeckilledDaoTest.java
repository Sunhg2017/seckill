package com.seckill.dao;

import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessSeckilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessSeckilledDaoTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SuccessSeckilledDao successSeckilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1001;
        long userPhone = 302270455;
        int i = successSeckilledDao.insertSuccessKilled(id,userPhone);
        logger.info("i={}",i);
    }

    @Test
    public void querryByIdWithSeckill() throws Exception {
        long id = 1001;
        long userPhone = 302270455;
        SuccessSeckilled successSeckilled = successSeckilledDao.querryByIdWithSeckill(id,userPhone);
        Seckill seckill = successSeckilled.getSeckill();
        logger.info("successSeckilled={}",successSeckilled);
        logger.info("seckill={}",seckill);
    }

}