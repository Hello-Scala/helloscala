package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.service.entity.JobLog;
import org.springframework.stereotype.Repository;


@Repository
public interface JobLogMapper extends BaseMapper<JobLog> {
    void clean();
}
