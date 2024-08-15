package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Category;
import com.helloscala.common.vo.category.SystemCategoryListVO;

import java.util.List;
import java.util.Set;


public interface CategoryService extends IService<Category> {
    Page<SystemCategoryListVO> selectCategoryPage(String name);

    Category getCategoryById(Long id);

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(List<Long> list);

    List<Category> listByIds(Set<String> categoryIds);
}
