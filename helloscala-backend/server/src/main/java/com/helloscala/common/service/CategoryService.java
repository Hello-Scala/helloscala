package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Category;

import java.util.List;


public interface CategoryService extends IService<Category> {
    ResponseResult selectCategoryPage(String name);

    ResponseResult getCategoryById(Long id);

    ResponseResult addCategory(Category category);

    ResponseResult updateCategory(Category category);

    ResponseResult deleteCategory(List<Long> list);
}
