package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    /**
     * 减库存操作
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀单
     * @param seckillId
     * @return
     */
    Seckill querryById(long seckillId);

    /**
     * 根据偏移量查询秒杀单
     * @param offet
     * @param limit
     * @return
     */
    List<Seckill> querryAll(@Param("offset") int offset,@Param("limit") int limit);

    void killByProcedure(Map<String,Object> paramMap);
}
