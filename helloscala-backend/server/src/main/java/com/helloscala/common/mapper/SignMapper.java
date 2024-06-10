package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.Sign;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SignMapper extends BaseMapper<Sign> {
    Sign selctSignByUserIdAndTime(@Param("userId") String userId, @Param("time")String time);

    List<String> getSignRecordsByUserId(@Param("startTime") String startTime, @Param("endTime")String endTime, @Param("userId")String userId);

    Sign validateTodayIsSign(@Param("time") String time, @Param("userId") String userId);
}
