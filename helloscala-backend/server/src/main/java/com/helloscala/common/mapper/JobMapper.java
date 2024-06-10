package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.Job;
import org.springframework.stereotype.Repository;


@Repository
public interface JobMapper extends BaseMapper<Job> {
}
