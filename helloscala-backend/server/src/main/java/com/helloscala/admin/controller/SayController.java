package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateSayRequest;
import com.helloscala.admin.controller.request.BOUpdateSayRequest;
import com.helloscala.admin.controller.view.BOSayView;
import com.helloscala.admin.service.BOSayService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

// todo name
@RestController
@RequestMapping("/system/say")
@Tag(name = "Says")
@RequiredArgsConstructor
public class SayController {

    private final BOSayService sayService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Operation(summary = "List says", method = "GET")
    @ApiResponse(responseCode = "200", description = "List says")
    public Response<Page<BOSayView>> selectSayPage(@RequestParam(name = "keywords", required = false) String keywords) {
        Page<BOSayView> sayPage = sayService.listByPage(keywords);
        return ResponseHelper.ok(sayPage);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @Operation(summary = "Get say detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get say detail")
    public Response<BOSayView> info(@RequestParam(name = "id", required = true) String id) {
        BOSayView say = sayService.getById(id);
        return ResponseHelper.ok(say);
    }

    @OperationLogger(value = "Update says")
    @SaCheckPermission("system:say:update")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @Operation(summary = "Update says", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update says")
    public EmptyResponse updateSay(@RequestBody BOUpdateSayRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        sayService.update(userId, request);
        return ResponseHelper.ok();
    }

    @OperationLogger(value = "Publish a say")
    @SaCheckPermission("system:say:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Operation(summary = "Publish a say", method = "POST")
    @ApiResponse(responseCode = "200", description = "发表说说")
    public EmptyResponse addSay(@RequestBody BOCreateSayRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        sayService.create(userId, request);
        return ResponseHelper.ok();
    }

    @OperationLogger(value = "Delete say")
    @SaCheckPermission("system:say:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @Operation(summary = "Delete say", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete say")
    public EmptyResponse deleteSay(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        sayService.bulkDelete(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }
}
