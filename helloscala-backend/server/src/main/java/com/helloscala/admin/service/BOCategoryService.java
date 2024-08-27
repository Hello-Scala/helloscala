package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOCreateCategoryRequest;
import com.helloscala.admin.controller.view.BOUpdateCategoryRequest;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.admin.controller.view.BOCategoryView;
import com.helloscala.service.service.CategoryService;
import com.helloscala.service.web.request.CreateCategoryRequest;
import com.helloscala.service.web.request.SearchCategoryRequest;
import com.helloscala.service.web.request.UpdateCategoryRequest;
import com.helloscala.service.web.view.CategoryView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Steve Zou
 */
@Service
@RequiredArgsConstructor
public class BOCategoryService {
    private final CategoryService categoryService;

    public Page<BOCategoryView> listByPage(String name) {
        Page<?> page = Page.of(PageUtil.getPageNo(), PageUtil.getPageSize());
        SearchCategoryRequest searchCategoryRequest = new SearchCategoryRequest();
        searchCategoryRequest.setName(name);
        Page<CategoryView> categoryPage = categoryService.listCategoryPage(page, searchCategoryRequest);

        return PageHelper.convertTo(categoryPage, category -> {
            BOCategoryView categoryView = new BOCategoryView();
            categoryView.setId(category.getId());
            categoryView.setName(category.getName());
            categoryView.setClickVolume(category.getClickVolume());
            categoryView.setSort(category.getSort());
            categoryView.setIcon(category.getIcon());
            categoryView.setArticleCount(category.getArticleCount());
            categoryView.setCreateTime(category.getCreateTime());
            categoryView.setUpdateTime(category.getUpdateTime());
            return categoryView;
        });
    }

    public BOCategoryView getById(String id) {
        CategoryView category = categoryService.getCategoryById(id);
        BOCategoryView categoryView = new BOCategoryView();
        categoryView.setId(category.getId());
        categoryView.setName(category.getName());
        categoryView.setClickVolume(category.getClickVolume());
        categoryView.setSort(category.getSort());
        categoryView.setIcon(category.getIcon());
        categoryView.setArticleCount(category.getArticleCount());
        categoryView.setCreateTime(category.getCreateTime());
        categoryView.setUpdateTime(category.getUpdateTime());
        return categoryView;
    }

    public void add(BOCreateCategoryRequest request) {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setName(request.getName());
        createCategoryRequest.setIcon(request.getIcon());
        createCategoryRequest.setSort(request.getSort());
        categoryService.addCategory(createCategoryRequest);
    }

    public void update(BOUpdateCategoryRequest request) {
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest();
        updateCategoryRequest.setId(request.getId());
        updateCategoryRequest.setName(request.getName());
        updateCategoryRequest.setIcon(request.getIcon());
        updateCategoryRequest.setSort(request.getSort());
        categoryService.updateCategory(updateCategoryRequest);
    }

    public void bulkDelete(Set<String> ids) {
        categoryService.bulkDeleteCategory(ids);
    }
}

