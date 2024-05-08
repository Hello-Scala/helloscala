package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Category;

import java.util.List;


public interface CategoryService extends IService<Category> {

    /**
     * 分类列表
     * @param name 分类名
     * @return
     */
    ResponseResult selectCategoryPage(String name);

    /**
     * 分类详情
     * @param id 分类id
     * @return
     */
    ResponseResult getCategoryById(Long id);

    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    ResponseResult addCategory(Category category);

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    ResponseResult updateCategory(Category category);


    /**
     * 批量删除分类
     * @param list 分类对象集合
     * @return
     */
    ResponseResult deleteCategory(List<Long> list);


}
