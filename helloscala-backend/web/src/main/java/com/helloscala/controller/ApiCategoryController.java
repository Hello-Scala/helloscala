package com.helloscala.controller;

import com.helloscala.common.ResponseResult;
import com.helloscala.service.ApiCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "分类API-V1")
@RestController
@RequestMapping("v1/category")
@RequiredArgsConstructor
public class ApiCategoryController {

    private final ApiCategoryService categoryService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "分类列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "分类列表")
    public ResponseResult selectCategoryList(){
        return categoryService.selectCategoryList();
    }
}
