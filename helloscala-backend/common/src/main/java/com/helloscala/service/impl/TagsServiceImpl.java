package com.helloscala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Tags;
import com.helloscala.exception.BusinessException;
import com.helloscala.mapper.TagsMapper;
import com.helloscala.service.TagsService;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.tag.SystemTagListVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

    /**
     * 标签列表
     * @param name
     * @return
     */
    @Override
    public ResponseResult selectTagsPage(String name) {
        Page<SystemTagListVo> list = baseMapper.selectPageRecord(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),name);
        return ResponseResult.success(list);
    }

    /**
     * 标签详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getTagsById(Long id) {
        Tags tags = baseMapper.selectById(id);
        return ResponseResult.success(tags);
    }

    /**
     * 添加标签
     * @param tags
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addTags(Tags tags) {
        validateName(tags.getName());
        baseMapper.insert(tags);
        return ResponseResult.success();
    }

    /**
     * 修改标签
     * @param tags
     * @return
     */
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

    /**
     * 批量删除标签
     * @param ids
     * @return
     */
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
            throw new BusinessException("标签下有文章，无法删除");
        }
    }

    public void validateName(String name){
        Tags entity = baseMapper.selectOne(new LambdaQueryWrapper<Tags>().eq(Tags::getName,name));
        if (ObjectUtils.isNotEmpty(entity)) {
            throw new BusinessException("标签名已存在");
        }
    }
}
