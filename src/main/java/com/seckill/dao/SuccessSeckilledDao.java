package com.seckill.dao;

import com.seckill.entity.SuccessSeckilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessSeckilledDao {
    /**
     * 插入秒杀记录
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
    /**
     *
     * @param seckillId
     * @returnd,long userPhone);

     */
    SuccessSeckilled querryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * @param state
     * @return
     */
    int updateState(@Param("state") int state,@Param("seckillId") long seckillId);
}
