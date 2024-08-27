package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.service.entity.Collect;
import com.helloscala.service.mapper.CollectMapper;
import com.helloscala.service.service.CollectService;
import com.helloscala.service.web.view.CollectCountView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Steve Zou
 */
@Service
@RequiredArgsConstructor
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    @Override
    public List<CollectCountView> countByArticles(Set<String> articleIds) {
        if (ObjectUtil.isEmpty(articleIds)) {
            return List.of();
        }
        return baseMapper.countByArticles(articleIds);
    }

    @Override
    public List<String> listCollectArticleIds(String userId) {
        if (StrUtil.isBlank(userId)) {
            return List.of();
        }
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Collect::getId, Collect::getUserId, Collect::getArticleId).eq(Collect::getUserId, userId);
        List<Collect> collects = baseMapper.selectList(queryWrapper);
        return collects.stream().map(Collect::getArticleId).toList();
    }
}
