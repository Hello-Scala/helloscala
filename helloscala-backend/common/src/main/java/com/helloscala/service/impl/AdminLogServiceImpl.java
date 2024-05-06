package com.helloscala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.AdminLog;
import com.helloscala.mapper.AdminLogMapper;
import com.helloscala.service.AdminLogService;
import com.helloscala.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {

    /**
     * 分页查询操作日志
     * @return
     */
    @Override
    public ResponseResult selectAdminLogPage() {
        Page<AdminLog> sysLogPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                new LambdaQueryWrapper<AdminLog>().orderByDesc(AdminLog::getCreateTime));
        return ResponseResult.success(sysLogPage);
    }

    /**
     * 批量删除操作日志
     * @param ids 操作日志id集合
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteAdminLog(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("批量删除操作日志失败");
    }
}
