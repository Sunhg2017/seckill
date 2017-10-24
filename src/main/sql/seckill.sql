DELIMITER $$
CREATE PROCEDURE `seckill`.`execute_seckill`
  (IN V_seckill_id BIGINT, IN V_phone BIGINT,IN V_kill_time TIMESTAMP,OUT r_result INT)
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION ;
    INSERT IGNORE INTO success_killed(seckill_id,user_phone,create_time)
      VALUES (V_seckill_id,V_phone,V_kill_time);
    SELECT row_count() INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK ;
      SET r_result = -1;
    ELSEIF (insert_count<0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill
        SET number = number-1
      WHERE seckill_id = V_seckill_id
      AND end_time > V_kill_time
      AND start_time < V_kill_time
      AND number > 0;
      SELECT row_count() INTO insert_count;
      IF (insert_count =  0) THEN
        ROLLBACK ;
        SET r_result = 0;
      ELSEIF (insert_count < 0) THEN
        ROLLBACK ;
        SET r_result = -2;
      ELSE
        COMMIT ;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$
DELIMITER ;
SET @r_result = -3;
CALL execute_seckill(1003,18302270465,now(),@r_result);
SELECT @r_result;
