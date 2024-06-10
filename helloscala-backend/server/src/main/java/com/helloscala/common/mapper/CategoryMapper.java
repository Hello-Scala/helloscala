package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Category;
import com.helloscala.common.vo.category.ApiCategoryListVO;
import com.helloscala.common.vo.category.SystemCategoryCountVO;
import com.helloscala.common.vo.category.SystemCategoryListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryMapper extends BaseMapper<Category> {
    Page<SystemCategoryListVO> selectCategory(@Param("page")Page<Category> objectPage, @Param("name")String name);

    List<SystemCategoryCountVO> countArticle();

    List<ApiCategoryListVO> selectCategoryListApi();
}
