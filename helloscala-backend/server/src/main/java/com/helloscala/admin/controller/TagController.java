package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateTagRequest;
import com.helloscala.admin.controller.request.BOUpdateTagRequest;
import com.helloscala.admin.controller.view.BOListTagView;
import com.helloscala.admin.controller.view.BOTagView;
import com.helloscala.admin.service.BOTagService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/system/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag management")
@RequiredArgsConstructor
public class TagController {

    private final BOTagService tagsService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Operation(summary = "List tag", method = "GET")
    @ApiResponse(responseCode = "200", description = "List tag")
    public Response<Page<BOListTagView>> listByName(@RequestParam(name = "name", required = false) String name) {
        Page<BOListTagView> tagPage = tagsService.listByName(name);
        return ResponseHelper.ok(tagPage);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("system:tags:add")
    @Operation(summary = "Add tag", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add tag")
    @OperationLogger(value = "Add tag")
    public EmptyResponse create(@RequestBody BOCreateTagRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        tagsService.create(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @Operation(summary = "Get tag detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get tag detail")
    public Response<BOTagView> getTagsById(@RequestParam(name = "id", required = true) String id) {
        BOTagView tagView = tagsService.get(id);
        return ResponseHelper.ok(tagView);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @SaCheckPermission("system:tags:update")
    @Operation(summary = "Update tag", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update tag")
    @OperationLogger(value = "Update tag")
    public EmptyResponse update(@RequestBody BOUpdateTagRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        tagsService.update(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:tags:delete")
    @Operation(summary = "Batch delete tag", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Batch delete tag")
    @OperationLogger(value = "Batch delete tag")
    public EmptyResponse deleteTags(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        tagsService.bulkDelete(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }
}

