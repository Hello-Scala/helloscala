package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Collect;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.mapper.CollectMapper;
import com.helloscala.common.mapper.TagsMapper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ApiArticleListVO;
import com.helloscala.web.service.ApiCollectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCollectServiceImpl implements ApiCollectService {

    private final CollectMapper collectMapper;

    private final TagsMapper tagsMapper;

    @Override
    public ResponseResult selectCollectList() {
        Page<ApiArticleListVO> list = collectMapper.selectCollectList(new Page<ApiArticleListVO>(PageUtil.getPageNo(), PageUtil.getPageSize()),StpUtil.getLoginIdAsString());
        list.getRecords().forEach(item ->{
            List<Tag> tags = tagsMapper.selectTagByArticleId(item.getId());
            item.setTagList(tags);
        });
        return ResponseResult.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult collect(Integer articleId) {
        Collect collect = Collect.builder().userId(StpUtil.getLoginIdAsString()).articleId(articleId).build();
        collectMapper.insert(collect);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult cancel(Integer articleId) {
        collectMapper.delete(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId,StpUtil.getLoginIdAsString()).eq(Collect::getArticleId,articleId));
        return ResponseResult.success();
    }
}
