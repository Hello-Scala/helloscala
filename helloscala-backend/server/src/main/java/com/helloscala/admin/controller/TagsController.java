package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Tags;
import com.helloscala.common.service.TagsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/tags")
@Tag(name = "Tag management")
@RequiredArgsConstructor
public class TagsController {

    private final TagsService tagsService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List tag", method = "GET")
    @ApiResponse(responseCode = "200", description = "List tag")
    public ResponseResult selectTagsPage(@RequestParam(name = "name", required = false) String name){
        return tagsService.selectTagsPage(name);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:tags:add")
    @Operation(summary = "Add tag", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add tag")
    @OperationLogger(value = "Add tag")
    public ResponseResult addTags(@RequestBody Tags tags){
        return tagsService.addTags(tags);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "Get tag detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get tag detail")
    public ResponseResult getTagsById(@RequestParam(name = "id", required = true) Long id){
        return tagsService.getTagsById(id);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:tags:update")
    @Operation(summary = "Update tag", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update tag")
    @OperationLogger(value = "Update tag")
    public ResponseResult update(@RequestBody Tags tags){
        return tagsService.updateTag(tags);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:tags:delete")
    @Operation(summary = "Batch delete tag", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete tag")
    @OperationLogger(value = "Batch delete tag")
    public ResponseResult deleteTags(@RequestBody List<Long> ids){
        return tagsService.deleteTags(ids);
    }
}

