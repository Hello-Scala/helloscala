package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.service.entity.Say;
import com.helloscala.service.enums.PublishEnum;
import com.helloscala.service.mapper.SayMapper;
import com.helloscala.service.service.SayService;
import com.helloscala.common.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@RequiredArgsConstructor
public class SayServiceImpl extends ServiceImpl<SayMapper, Say> implements SayService {


    @Override
    public Page<Say> selectSayPage(String keywords) {
        LambdaQueryWrapper<Say> sayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StpUtil.hasRole(Constants.ADMIN_CODE)) {
            sayLambdaQueryWrapper.eq(Say::getIsPublic, PublishEnum.PUBLISH);
        }
        sayLambdaQueryWrapper.orderByDesc(Say::getCreateTime);
        Page<Say> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPage(page,sayLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSay(Say say) {
        say.setUserId(StpUtil.getLoginIdAsString());
        baseMapper.insert(say);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSay(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public Say selectSayById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSay(Say say) {
        baseMapper.updateById(say);
    }
}
