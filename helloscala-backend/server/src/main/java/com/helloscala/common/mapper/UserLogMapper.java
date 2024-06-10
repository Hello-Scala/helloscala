package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.UserLog;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface UserLogMapper extends BaseMapper<UserLog> {

    @Select("select count(distinct ip) from b_user_log where DATE_FORMAT(create_time,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')")
    Integer getToday();

    List<Map<String,Object>> getUserAccess(String date);
}
