package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.service.TagService;
import com.helloscala.common.vo.tag.SystemTagListVo;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag management")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagsService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List tag", method = "GET")
    @ApiResponse(responseCode = "200", description = "List tag")
    public Response<Page<SystemTagListVo>> listByName(@RequestParam(name = "name", required = false) String name){
        Page<SystemTagListVo> tagPage = tagsService.selectByName(name);
        return ResponseHelper.ok(tagPage);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:tags:add")
    @Operation(summary = "Add tag", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add tag")
    @OperationLogger(value = "Add tag")
    public EmptyResponse addTags(@RequestBody Tag tags){
        tagsService.addTags(tags);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @Operation(summary = "Get tag detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get tag detail")
    public Response<Tag> getTagsById(@RequestParam(name = "id", required = true) Long id){
        Tag tag = tagsService.getTagsById(id);
        return ResponseHelper.ok(tag);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:tags:update")
    @Operation(summary = "Update tag", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update tag")
    @OperationLogger(value = "Update tag")
    public EmptyResponse update(@RequestBody Tag tags){
        tagsService.updateTag(tags);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:tags:delete")
    @Operation(summary = "Batch delete tag", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete tag")
    @OperationLogger(value = "Batch delete tag")
    public EmptyResponse deleteTags(@RequestBody List<Long> ids){
        tagsService.deleteTags(ids);
        return ResponseHelper.ok();
    }
}

