package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Category;
import com.helloscala.service.mapper.CategoryMapper;
import com.helloscala.service.service.ArticleService;
import com.helloscala.service.service.CategoryService;
import com.helloscala.service.web.request.CreateCategoryRequest;
import com.helloscala.service.web.request.SearchCategoryRequest;
import com.helloscala.service.web.request.UpdateCategoryRequest;
import com.helloscala.service.web.view.CategoryArticleCountView;
import com.helloscala.service.web.view.CategoryView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    private final ArticleService articleService;

    @Override
    public Page<CategoryView> listCategoryPage(Page<?> page, SearchCategoryRequest request) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ObjectUtil.isNotEmpty(request.getIds()), Category::getId, request.getIds());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), Category::getName, request.getName());
        queryWrapper.orderByDesc(Category::getCreateTime);
        Page<Category> categoryPage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);

        Set<String> categoryIds = categoryPage.getRecords().stream().map(Category::getId).collect(Collectors.toSet());
        List<CategoryArticleCountView> categoryCountList = articleService.countByCategories(categoryIds);
        Map<String, CategoryArticleCountView> categoryCountMap = categoryCountList.stream().collect(Collectors.toMap(CategoryArticleCountView::getId, Function.identity()));

        return PageHelper.convertTo(categoryPage, category -> {
            CategoryArticleCountView countView = categoryCountMap.get(category.getId());
            long count = Optional.ofNullable(countView).map(CategoryArticleCountView::getCount).orElse(0L);

            CategoryView categoryView = new CategoryView();
            categoryView.setId(category.getId());
            categoryView.setName(category.getName());
            categoryView.setClickVolume(category.getClickVolume());
            categoryView.setSort(category.getSort());
            categoryView.setIcon(category.getIcon());
            categoryView.setArticleCount((int) count);
            categoryView.setCreateTime(category.getCreateTime());
            categoryView.setUpdateTime(category.getUpdateTime());
            return categoryView;
        });
    }

    @Override
    public List<CategoryView> listCategoryByIds(Set<String> ids) {
        List<Category> categories = listByIds(ids);
        return categories.stream().map(category -> {
            CategoryView categoryView = new CategoryView();
            categoryView.setId(category.getId());
            categoryView.setName(category.getName());
            categoryView.setClickVolume(category.getClickVolume());
            categoryView.setSort(category.getSort());
            categoryView.setIcon(category.getIcon());
            categoryView.setArticleCount(0);
            categoryView.setCreateTime(category.getCreateTime());
            categoryView.setUpdateTime(category.getUpdateTime());
            return categoryView;
        }).toList();
    }

    @Override
    public CategoryView getCategoryById(String id) {

        Category category = baseMapper.selectById(id);
        if (Objects.isNull(category)) {
            throw new NotFoundException("Category not found, id={}!", id);
        }
        List<CategoryArticleCountView> categoryCountList = articleService.countByCategories(Set.of(id));
        Map<String, CategoryArticleCountView> categoryCountMap = categoryCountList.stream().collect(Collectors.toMap(CategoryArticleCountView::getId, Function.identity()));

        CategoryArticleCountView countView = categoryCountMap.get(category.getId());
        long count = Optional.ofNullable(countView).map(CategoryArticleCountView::getCount).orElse(0L);

        CategoryView categoryView = new CategoryView();
        categoryView.setId(category.getId());
        categoryView.setName(category.getName());
        categoryView.setClickVolume(category.getClickVolume());
        categoryView.setSort(category.getSort());
        categoryView.setIcon(category.getIcon());
        categoryView.setArticleCount((int) count);
        categoryView.setCreateTime(category.getCreateTime());
        categoryView.setUpdateTime(category.getUpdateTime());
        return categoryView;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CreateCategoryRequest request) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>().eq(Category::getName, request.getName());
        boolean nameExist = baseMapper.exists(queryWrapper);
        if (nameExist) {
            throw new ConflictException("Category exist, name={}", request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setIcon(request.getIcon());
        category.setSort(request.getSort());
        category.setClickVolume(0);
        category.setCreateTime(new Date());
        int insert = baseMapper.insert(category);
        if (insert <= 0) {
            throw new ConflictException("Failed to create category, category={}", JSONObject.toJSON(category));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(UpdateCategoryRequest request) {
        Category category = baseMapper.selectById(request.getId());
        if (Objects.isNull(category)) {
            throw new NotFoundException("Category not found, id={}!", request.getId());
        }
        category.setName(request.getName());
        category.setIcon(request.getIcon());
        category.setSort(request.getSort());
        category.setUpdateTime(new Date());
        int update = baseMapper.updateById(category);
        if (update <= 0) {
            throw new ConflictException("Failed to update category, category={}", JSONObject.toJSON(category));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bulkDeleteCategory(Set<String> categoryIds) {

        boolean existAnyUnderCategory = articleService.existAnyUnderCategory(categoryIds);
        if (existAnyUnderCategory) {
            throw new ConflictException("Category delete failed, exist articles under categoryIds=[{}]!", String.join(",", categoryIds));
        }
        int deleted = baseMapper.deleteBatchIds(categoryIds);
        if (deleted <= 0) {
            throw new ConflictException("Failed to delete categories, category ids=[{}]!", String.join(",", categoryIds));
        }
    }
}
