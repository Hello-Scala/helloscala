package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.entity.JobLog;
import org.springframework.stereotype.Repository;


@Repository
public interface JobLogMapper extends BaseMapper<JobLog> {

    void clean();

}
