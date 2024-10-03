package com.helloscala.web.controller;

import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.controller.category.APICategoryView;
import com.helloscala.web.service.APICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "Category API-V1")
@RestController
@RequestMapping("v1/category")
@RequiredArgsConstructor
public class ApiCategoryController {
    private final APICategoryService categoryService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "List categories", method = "GET")
    @ApiResponse(responseCode = "200", description = "List categories")
    public Response<List<APICategoryView>> list(){
        List<APICategoryView> categoryViews = categoryService.list();
        return ResponseHelper.ok(categoryViews);
    }
}
