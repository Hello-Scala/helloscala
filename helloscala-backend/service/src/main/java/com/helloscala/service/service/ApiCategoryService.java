package com.helloscala.service.service;

import com.helloscala.common.vo.category.ApiCategoryListVO;

import java.util.List;

public interface ApiCategoryService {
    List<ApiCategoryListVO> selectCategoryList();
}
