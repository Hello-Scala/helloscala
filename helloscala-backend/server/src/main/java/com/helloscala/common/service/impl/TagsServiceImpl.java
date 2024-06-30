package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.mapper.TagsMapper;
import com.helloscala.common.service.TagsService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.tag.SystemTagListVo;
import com.helloscala.common.web.exception.ConflictException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tag> implements TagsService {

    @Override
    public Page<SystemTagListVo> selectByName(String name) {
        Page<Tag> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        return baseMapper.selectPageRecord(page, name);
    }

    @Override
    public Tag getTagsById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTags(Tag tags) {
        validateName(tags.getName());
        baseMapper.insert(tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(Tag tags) {
        Tag tag = baseMapper.selectById(tags.getId());
        if (!tag.getName().equalsIgnoreCase(tags.getName())) {
            validateName(tags.getName());
        }
        baseMapper.updateById(tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTags(List<Long> ids) {
        for (Long id : ids) {
            validateTagIdIsExistArticle(id);
        }
        baseMapper.deleteBatchIds(ids);
    }

    private void validateTagIdIsExistArticle(Long id) {
        int count = baseMapper.validateTagIdIsExistArticle(id);
        if (count > 0) {
            throw new ConflictException("Delete tag failed, exist articles under this tag!");
        }
    }

    public void validateName(String name) {
        Tag entity = baseMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
        if (ObjectUtils.isNotEmpty(entity)) {
            throw new ConflictException("Tag exist!");
        }
    }
}
