package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.mapper.SystemConfigMapper;
import com.helloscala.common.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {
    @Override
    public ResponseResult getSystemConfig() {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<SystemConfig>().last(Constants.LIMIT_ONE);

        if (!StpUtil.hasRole(Constants.ADMIN_CODE)){
            wrapper.select(SystemConfig::getId,SystemConfig::getOpenEmail,SystemConfig::getOpenEmailActivate,SystemConfig::getStartEmailNotification,
                    SystemConfig::getFileUploadWay,SystemConfig::getDashboardNotification,SystemConfig::getDashboardNotificationMd,
                    SystemConfig::getOpenDashboardNotification,SystemConfig::getSearchModel,SystemConfig::getLocalFileUrl,
                    SystemConfig::getEmailHost,SystemConfig::getEmailPort);
        }

        SystemConfig systemConfig = baseMapper.selectOne(wrapper);

        return ResponseResult.success(systemConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSystemConfig(SystemConfig systemConfig) {
        baseMapper.updateById(systemConfig);
        return ResponseResult.success();
    }

    @Override
    public SystemConfig getCustomizeOne() {
        return baseMapper.selectOne(new QueryWrapper<SystemConfig>().last(Constants.LIMIT_ONE));
    }
}
