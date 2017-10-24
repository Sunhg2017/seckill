package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
        long id = 1000;
        Date killTime = new Date();
        int i = seckillDao.reduceNumber(id,killTime);
        logger.info("i={}",i);
    }

    @Test
    public void querryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.querryById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void querryAll() throws Exception {
        List<Seckill> list = seckillDao.querryAll(0,100);
        logger.info("list={}",list);
    }

}