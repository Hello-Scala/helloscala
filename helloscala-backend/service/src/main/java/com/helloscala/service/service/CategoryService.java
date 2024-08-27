package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Category;
import com.helloscala.service.web.request.CreateCategoryRequest;
import com.helloscala.service.web.request.SearchCategoryRequest;
import com.helloscala.service.web.request.UpdateCategoryRequest;
import com.helloscala.service.web.view.CategoryView;

import java.util.Set;


public interface CategoryService extends IService<Category> {
    Page<CategoryView> listCategoryPage(Page<?> page, SearchCategoryRequest request);

    CategoryView getCategoryById(String id);

    void addCategory(CreateCategoryRequest request);

    void updateCategory(UpdateCategoryRequest request);

    void bulkDeleteCategory(Set<String> categoryIds);
}
