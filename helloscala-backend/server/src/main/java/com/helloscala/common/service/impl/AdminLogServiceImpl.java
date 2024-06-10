package com.helloscala.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.AdminLog;
import com.helloscala.common.mapper.AdminLogMapper;
import com.helloscala.common.service.AdminLogService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {

    @Override
    public ResponseResult selectAdminLogPage() {
        Page<AdminLog> sysLogPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                new LambdaQueryWrapper<AdminLog>().orderByDesc(AdminLog::getCreateTime));
        return ResponseResult.success(sysLogPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteAdminLog(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error(StrUtil.format("Failed to dilete admin logs, ids=[{}]!", StrUtil.join(", ", ids)));
    }
}
