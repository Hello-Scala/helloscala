package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.service.entity.Followed;
import org.springframework.stereotype.Repository;


@Repository
public interface FollowedMapper extends BaseMapper<Followed> {
    int countQiDay(String id);
}
