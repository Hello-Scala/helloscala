package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Collect;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.mapper.CollectMapper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.web.service.ApiArticleService;
import com.helloscala.web.service.ApiCollectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCollectServiceImpl implements ApiCollectService {
    private final ApiArticleService articleService;
    private final CollectMapper collectMapper;

    @Override
    public Page<RecommendedArticleVO> selectCollectList(String userId) {
        Page<RecommendedArticleVO> recommendedArticleVOPage = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<RecommendedArticleVO> list = collectMapper.selectCollectList(recommendedArticleVOPage, userId);

        List<RecommendedArticleVO> records = list.getRecords();
        Set<String> articleIds = records.stream().map(RecommendedArticleVO::getId).collect(Collectors.toSet());
        Map<String, List<Tag>> articleTagListMap = articleService.getArticleTagListMap(articleIds);
        records.forEach(item -> item.setTagList(articleTagListMap.get(item.getId())));
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collect(String userId, String articleId) {
        Collect collect = Collect.builder().userId(userId).articleId(articleId).build();
        collectMapper.insert(collect);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String userId, String articleId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId).eq(Collect::getArticleId, articleId);
        collectMapper.delete(queryWrapper);
    }
}
