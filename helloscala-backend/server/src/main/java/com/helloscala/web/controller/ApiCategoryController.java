package com.helloscala.web.controller;

import com.helloscala.common.ResponseResult;
import com.helloscala.web.service.ApiCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Category API-V1")
@RestController
@RequestMapping("v1/category")
@RequiredArgsConstructor
public class ApiCategoryController {

    private final ApiCategoryService categoryService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "List categories", method = "GET")
    @ApiResponse(responseCode = "200", description = "List categories")
    public ResponseResult selectCategoryList(){
        return categoryService.selectCategoryList();
    }
}
