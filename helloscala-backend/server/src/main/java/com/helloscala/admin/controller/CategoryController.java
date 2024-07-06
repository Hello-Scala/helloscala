package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.Category;
import com.helloscala.common.service.CategoryService;
import com.helloscala.common.vo.category.SystemCategoryListVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<Page<SystemCategoryListVO>> selectCategoryPage(@RequestParam(name = "name", required = false) String name){
        Page<SystemCategoryListVO> categoryPage = categoryService.selectCategoryPage(name);
        return ResponseHelper.ok(categoryPage);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "Get category by id", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get category by id")
    public Response<Category> getCategoryById(@RequestParam(name = "id", required = true) Long id){
        Category category = categoryService.getCategoryById(id);
        return ResponseHelper.ok(category);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:category:add")
    @OperationLogger(value = "Add category")
    @Operation(summary = "Add category", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add category")
    public EmptyResponse addCategory(@RequestBody Category category){
        categoryService.addCategory(category);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:category:update")
    @Operation(summary = "Update category", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update category")
    @OperationLogger(value = "Update category")
    public EmptyResponse update(@RequestBody Category category){
        categoryService.updateCategory(category);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:category:delete")
    @Operation(summary = "Bulk delete categories", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Bulk delete categories")
    @OperationLogger(value = "Bulk delete categories")
    public EmptyResponse deleteCategory(@RequestBody List<Long> list){
        categoryService.deleteCategory(list);
        return ResponseHelper.ok();
    }
}

