package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.ListHelper;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.service.entity.Tag;
import com.helloscala.service.mapper.TagMapper;
import com.helloscala.service.service.TagService;
import com.helloscala.service.web.request.CreateTagRequest;
import com.helloscala.service.web.request.UpdateTagRequest;
import com.helloscala.service.web.view.TagView;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public Page<TagView> listByName(Page<?> page, String name) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Tag::getName, name);
        Page<Tag> tagPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        return PageHelper.convertTo(tagPage, TagServiceImpl::buildTagView);
    }

    private static TagView buildTagView(Tag tag) {
        if (Objects.isNull(tag)) {
            return null;
        }
        TagView tagView = new TagView();
        tagView.setId(tag.getId());
        tagView.setName(tag.getName());
        tagView.setSort(tag.getSort());
        tagView.setClickVolume(tag.getClickVolume());
        tagView.setCreateTime(tag.getCreateTime());
        tagView.setUpdateTime(tag.getUpdateTime());
        return tagView;
    }

    @Override
    public TagView getTagsById(String id) {
        Tag tag = baseMapper.selectById(id);
        return buildTagView(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTag(CreateTagRequest request) {
        validateName(request.getName());

        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setSort(request.getSort());
        tag.setClickVolume(0);
        tag.setCreateTime(new Date());
        baseMapper.insert(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(UpdateTagRequest request) {
        Tag tag = baseMapper.selectById(request.getId());
        if (!tag.getName().equalsIgnoreCase(request.getName())) {
            validateName(request.getName());
        }
        tag.setName(request.getName());
        tag.setSort(request.getSort());
        tag.setUpdateTime(new Date());
        baseMapper.updateById(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTags(Set<String> ids) {
        for (String id : ids) {
            validateTagIdIsExistArticle(id);
            removeById(id);
        }
    }

    @Override
    public List<TagView> listByIds(Set<String> idSet) {
        if (ObjectUtil.isEmpty(idSet)) {
            return List.of();
        }
        List<Tag> tags = baseMapper.selectBatchIds(idSet);
        return ListHelper.ofNullable(tags).stream().map(TagServiceImpl::buildTagView).toList();
    }

    @Override
    public List<TagView> listByNames(Set<String> nameSet) {
        if (ObjectUtil.isEmpty(nameSet)) {
            return List.of();
        }
        LambdaQueryWrapper<Tag> tagQuery = new LambdaQueryWrapper<>();
        tagQuery.in(Tag::getName, nameSet);
        List<Tag> tags = baseMapper.selectList(tagQuery);
        return ListHelper.ofNullable(tags).stream().map(TagServiceImpl::buildTagView).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TagView> bulkCreateByNames(Set<String> nameSet) {
        if (ObjectUtil.isEmpty(nameSet)) {
            return List.of();
        }

        List<Tag> tagToCreateList = nameSet.stream()
                .map(name -> Tag.builder().name(name).sort(0).build()).toList();
        saveBatch(tagToCreateList);
        return ListHelper.ofNullable(tagToCreateList).stream().map(TagServiceImpl::buildTagView).toList();
    }

    private void validateTagIdIsExistArticle(String id) {
        int count = baseMapper.validateTagIdIsExistArticle(id);
        if (count > 0) {
            throw new ConflictException("Delete tag failed, exist articles under this tag!");
        }
    }

    private void validateName(String name) {
        Tag entity = baseMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
        if (ObjectUtils.isNotEmpty(entity)) {
            throw new ConflictException("Tag exist!");
        }
    }
}
