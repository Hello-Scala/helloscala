package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.ResultCode;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.Category;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.CategoryMapper;
import com.helloscala.common.service.CategoryService;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.category.SystemCategoryListVO;
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
    public ResponseResult selectCategoryPage(String name) {
        Page<SystemCategoryListVO> categoryPage = baseMapper.selectCategory(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), name);
        return ResponseResult.success(categoryPage);
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = baseMapper.selectById(id);
        return ResponseResult.success(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo != null) {
            throw new BusinessException(ResultCode.CATEGORY_IS_EXIST);
        }
        baseMapper.insert(category);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, category.getName()));
        if (vo!= null && !vo.getId().equals(category.getId())) {
            throw new BusinessException(ResultCode.CATEGORY_IS_EXIST);
        }

        baseMapper.updateById(category);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCategory(List<Long> list) {
        List<Long> ids = new ArrayList<>();
        for (Long id : list) {
            Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, id));
            if (count > 0) {
                throw new BusinessException( id + "category delete failed, exist articles under this category!");
            }
            ids.add(id);
        }

        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}
