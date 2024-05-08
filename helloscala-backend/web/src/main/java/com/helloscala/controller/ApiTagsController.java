package com.helloscala.controller;


import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.service.ApiTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/tag")
@Tag(name = "标签分类接口")
@RequiredArgsConstructor
public class ApiTagsController {

    private final ApiTagService tagsService;

    @BusinessLogger(value = "标签模块-用户访问页面",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @Operation(summary = "标签列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "标签列表")
    public ResponseResult selectTagList(){
        return tagsService.selectTagList();
    }

}

