package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Category;
import com.helloscala.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/category")
@RequiredArgsConstructor
@Tag(name = "分类管理")
public class CategoryController {

    private final CategoryService categoryService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "分类列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "分类列表")
    public ResponseResult selectCategoryPage(@RequestParam(name = "name", required = false) String name){
        return categoryService.selectCategoryPage(name);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "分类详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "分类详情")
    public ResponseResult getCategoryById(@RequestParam(name = "id", required = true) Long id){
        return categoryService.getCategoryById(id);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:category:add")
    @OperationLogger(value = "新增分类")
    @Operation(summary = "新增分类", method = "POST")
    @ApiResponse(responseCode = "200", description = "新增分类")
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:category:update")
    @Operation(summary = "修改分类", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改分类")
    @OperationLogger(value = "修改分类")
    public ResponseResult update(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:category:delete")
    @Operation(summary = "批量删除分类", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除分类")
    @OperationLogger(value = "批量删除分类")
    public ResponseResult deleteCategory(@RequestBody List<Long> list){
        return categoryService.deleteCategory(list);
    }

}

