package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.service.entity.Collect;
import com.helloscala.service.mapper.CollectMapper;
import com.helloscala.service.service.CollectService;
import com.helloscala.service.web.view.CollectCountView;
import com.helloscala.service.web.view.CollectView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public Page<CollectView> listByUserId(Page<?> page, String userId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId);
        Page<Collect> collectPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(collectPage, collect -> {
            CollectView collectView = new CollectView();
            collectView.setId(collect.getId());
            collectView.setUserId(collect.getUserId());
            collectView.setArticleId(collect.getArticleId());
            collectView.setCreateTime(collect.getCreateTime());
            return collectView;
        });
    }

    @Override
    public void collect(String userId, String articleId) {
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setArticleId(articleId);
        collect.setCreateTime(new Date());
        baseMapper.insert(collect);
    }

    @Override
    public void cancel(String userId, String articleId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId)
                .eq(Collect::getArticleId, articleId);
        baseMapper.delete(queryWrapper);
    }
}
