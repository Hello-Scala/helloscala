package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.WebConfig;
import com.helloscala.common.mapper.WebConfigMapper;
import com.helloscala.common.service.WebConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {
    @Override
    public ResponseResult getWebConfig() {
        WebConfig webConfig = baseMapper.selectOne(new QueryWrapper<WebConfig>()
                .last(Constants.LIMIT_ONE));
        return ResponseResult.success(webConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateWebConfig(WebConfig webConfig) {
        baseMapper.updateById(webConfig);
        return ResponseResult.success();
    }
}
