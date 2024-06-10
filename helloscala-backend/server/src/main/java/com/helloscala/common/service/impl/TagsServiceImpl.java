package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Tags;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.mapper.TagsMapper;
import com.helloscala.common.service.TagsService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.tag.SystemTagListVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

    @Override
    public ResponseResult selectTagsPage(String name) {
        Page<SystemTagListVo> list = baseMapper.selectPageRecord(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),name);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult getTagsById(Long id) {
        Tags tags = baseMapper.selectById(id);
        return ResponseResult.success(tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addTags(Tags tags) {
        validateName(tags.getName());
        baseMapper.insert(tags);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateTag(Tags tags) {
        Tags entity = baseMapper.selectById(tags.getId());
        if (!entity.getName().equalsIgnoreCase(tags.getName())){
            validateName(tags.getName());
        }
        baseMapper.updateById(tags);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteTags(List<Long> ids) {
        for (Long id : ids) {
            validateTagIdIsExistArticle(id);
        }
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    private void validateTagIdIsExistArticle(Long id) {
        int count = baseMapper.validateTagIdIsExistArticle(id);
        if (count > 0){
            throw new BusinessException("Delete tag failed, exist articles under this tag!");
        }
    }

    public void validateName(String name){
        Tags entity = baseMapper.selectOne(new LambdaQueryWrapper<Tags>().eq(Tags::getName,name));
        if (ObjectUtils.isNotEmpty(entity)) {
            throw new BusinessException("Tag exist!");
        }
    }
}
