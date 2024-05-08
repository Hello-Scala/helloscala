package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.entity.Followed;
import org.springframework.stereotype.Repository;


@Repository
public interface FollowedMapper extends BaseMapper<Followed> {

    /**
     * 统计当前用户七天的关注量
     * @param id
     * @return
     */
    int countQiDay(String id);
}
