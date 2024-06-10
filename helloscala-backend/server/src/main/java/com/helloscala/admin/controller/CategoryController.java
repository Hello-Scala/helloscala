package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Category;
import com.helloscala.common.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/category")
@RequiredArgsConstructor
@Tag(name = "Category management")
public class CategoryController {

    private final CategoryService categoryService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List category", method = "GET")
    @ApiResponse(responseCode = "200", description = "List category")
    public ResponseResult selectCategoryPage(@RequestParam(name = "name", required = false) String name){
        return categoryService.selectCategoryPage(name);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "Get category by id", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get category by id")
    public ResponseResult getCategoryById(@RequestParam(name = "id", required = true) Long id){
        return categoryService.getCategoryById(id);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:category:add")
    @OperationLogger(value = "Add category")
    @Operation(summary = "Add category", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add category")
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:category:update")
    @Operation(summary = "Update category", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update category")
    @OperationLogger(value = "Update category")
    public ResponseResult update(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:category:delete")
    @Operation(summary = "Bulk delete categories", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Bulk delete categories")
    @OperationLogger(value = "Bulk delete categories")
    public ResponseResult deleteCategory(@RequestBody List<Long> list){
        return categoryService.deleteCategory(list);
    }
}

