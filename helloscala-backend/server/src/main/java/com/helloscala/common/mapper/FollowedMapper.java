package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.Followed;
import org.springframework.stereotype.Repository;


@Repository
public interface FollowedMapper extends BaseMapper<Followed> {
    int countQiDay(String id);
}
