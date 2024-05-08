package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.entity.Category;
import com.helloscala.vo.category.ApiCategoryListVO;
import com.helloscala.vo.category.SystemCategoryCountVO;
import com.helloscala.vo.category.SystemCategoryListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 分页获取分类
     * @param objectPage 分页对象
     * @param name 分类名
     * @return
     */
    Page<SystemCategoryListVO> selectCategory(@Param("page")Page<Category> objectPage, @Param("name")String name);

    /**
     * 统计分类
     * @return
     */
    List<SystemCategoryCountVO> countArticle();

    List<ApiCategoryListVO> selectCategoryListApi();


}
