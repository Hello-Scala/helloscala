package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Collect;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.mapper.CollectMapper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ListArticleVO;
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
    public Page<ListArticleVO> selectCollectList() {
        Page<ListArticleVO> list = collectMapper.selectCollectList(new Page<ListArticleVO>(PageUtil.getPageNo(), PageUtil.getPageSize()),StpUtil.getLoginIdAsString());

        List<ListArticleVO> records = list.getRecords();
        Set<Long> articleIds = records.stream().map(ListArticleVO::getId).collect(Collectors.toSet());
        Map<Long, List<Tag>> articleTagListMap = articleService.getArticleTagListMap(articleIds);
        records.forEach(item -> item.setTagList(articleTagListMap.get(item.getId())));
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collect(Integer articleId) {
        Collect collect = Collect.builder().userId(StpUtil.getLoginIdAsString()).articleId(articleId).build();
        collectMapper.insert(collect);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Integer articleId) {
        collectMapper.delete(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId,StpUtil.getLoginIdAsString()).eq(Collect::getArticleId,articleId));
    }
}
