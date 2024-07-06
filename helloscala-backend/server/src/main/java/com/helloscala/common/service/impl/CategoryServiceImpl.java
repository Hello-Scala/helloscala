package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResultCode;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.Category;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.CategoryMapper;
import com.helloscala.common.service.CategoryService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.category.SystemCategoryListVO;
import com.helloscala.common.web.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleMapper articleMapper;

    @Override
    public Page<SystemCategoryListVO> selectCategoryPage(String name) {
        return baseMapper.selectCategory(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), name);
    }

    @Override
    public Category getCategoryById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo != null) {
            throw new ConflictException(ResultCode.CATEGORY_IS_EXIST.desc);
        }
        baseMapper.insert(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo!= null && !vo.getId().equals(category.getId())) {
            throw new ConflictException(ResultCode.CATEGORY_IS_EXIST.desc);
        }

        baseMapper.updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(List<Long> list) {
        List<Long> ids = new ArrayList<>();
        for (Long id : list) {
            Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, id));
            if (count > 0) {
                throw new ConflictException( id + "category delete failed, exist articles under this category!");
            }
            ids.add(id);
        }

        baseMapper.deleteBatchIds(ids);
    }
}
