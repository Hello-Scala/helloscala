package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.entity.WebConfig;
import com.helloscala.common.mapper.WebConfigMapper;
import com.helloscala.common.service.WebConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {
    @Override
    public WebConfig getWebConfig() {
        LambdaQueryWrapper<WebConfig> queryWrapper = new LambdaQueryWrapper<WebConfig>()
                .orderByDesc(WebConfig::getCreateTime)
                .last(Constants.LIMIT_ONE);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWebConfig(WebConfig webConfig) {
        baseMapper.updateById(webConfig);
    }
}
