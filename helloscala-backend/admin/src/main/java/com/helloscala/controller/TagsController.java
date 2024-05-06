package com.helloscala.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Tags;
import com.helloscala.service.TagsService;
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
@RequestMapping("/system/tags")
@Tag(name = "标签管理")
@RequiredArgsConstructor
public class TagsController {

    private final TagsService tagsService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "标签列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "标签列表")
    public ResponseResult selectTagsPage(@RequestParam(name = "name", required = false) String name){
        return tagsService.selectTagsPage(name);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:tags:add")
    @Operation(summary = "新增标签", method = "POST")
    @ApiResponse(responseCode = "200", description = "新增标签")
    @OperationLogger(value = "新增标签")
    public ResponseResult addTags(@RequestBody Tags tags){
        return tagsService.addTags(tags);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "标签详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "标签详情")
    public ResponseResult getTagsById(@RequestParam(name = "id", required = true) Long id){
        return tagsService.getTagsById(id);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:tags:update")
    @Operation(summary = "修改标签", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改标签")
    @OperationLogger(value = "修改标签")
    public ResponseResult update(@RequestBody Tags tags){
        return tagsService.updateTag(tags);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:tags:delete")
    @Operation(summary = "批量删除标签", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "批量删除标签")
    @OperationLogger(value = "批量删除标签")
    public ResponseResult deleteTags(@RequestBody List<Long> ids){
        return tagsService.deleteTags(ids);
    }

}

