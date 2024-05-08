package com.helloscala.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Say;
import com.helloscala.enums.PublishEnum;
import com.helloscala.mapper.SayMapper;
import com.helloscala.service.SayService;
import com.helloscala.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@RequiredArgsConstructor
public class SayServiceImpl extends ServiceImpl<SayMapper, Say> implements SayService {


    @Override
    public ResponseResult selectSayPage(String keywords) {
        LambdaQueryWrapper<Say> sayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StpUtil.hasRole(Constants.ADMIN_CODE)) {
            sayLambdaQueryWrapper.eq(Say::getIsPublic, PublishEnum.PUBLISH);
        }
        sayLambdaQueryWrapper.orderByDesc(Say::getCreateTime);
        Page<Say> sayPage = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),sayLambdaQueryWrapper);
        return ResponseResult.success(sayPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addSay(Say say) {
        say.setUserId(StpUtil.getLoginIdAsString());
        baseMapper.insert(say);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteSay(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult selectSayById(String id) {
        Say say = baseMapper.selectById(id);
        return ResponseResult.success(say);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSay(Say say) {
        baseMapper.updateById(say);
        return ResponseResult.success();
    }
}
