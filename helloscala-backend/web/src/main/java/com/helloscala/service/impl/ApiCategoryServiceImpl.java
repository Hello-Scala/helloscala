package com.helloscala.service.impl;

import com.helloscala.common.ResponseResult;
import com.helloscala.mapper.CategoryMapper;
import com.helloscala.service.ApiCategoryService;
import com.helloscala.vo.category.ApiCategoryListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiCategoryServiceImpl implements ApiCategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 首页分类列表
     * @return
     */
    @Override
    public ResponseResult selectCategoryList() {
        List<ApiCategoryListVO> list = categoryMapper.selectCategoryListApi();
        return ResponseResult.success(list);
    }
}
