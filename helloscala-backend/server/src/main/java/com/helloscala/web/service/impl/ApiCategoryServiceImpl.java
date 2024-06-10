package com.helloscala.web.service.impl;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.mapper.CategoryMapper;
import com.helloscala.common.vo.category.ApiCategoryListVO;
import com.helloscala.web.service.ApiCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiCategoryServiceImpl implements ApiCategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public ResponseResult selectCategoryList() {
        List<ApiCategoryListVO> list = categoryMapper.selectCategoryListApi();
        return ResponseResult.success(list);
    }
}
