package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOAddFriendLinkRequest;
import com.helloscala.admin.controller.request.BOUpdateFriendLinkRequest;
import com.helloscala.admin.controller.view.BOFriendLinkView;
import com.helloscala.admin.service.BOFriendLinkService;
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


@RestController
@RequestMapping("/system/friend")
@Tag(name = "Link management")
@RequiredArgsConstructor
public class FriendLinkController {

    private final BOFriendLinkService friendLinkService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Operation(summary = "List link", method = "GET")
    @ApiResponse(responseCode = "200", description = "友链列表")
    public Response<Page<BOFriendLinkView>> selectFriendLinkPage(@RequestParam(name = "name", required = false) String name,
                                                                 @RequestParam(name = "status", required = false) Integer status) {
        Page<BOFriendLinkView> friendLinkPage = friendLinkService.listByPage(name, status);
        return ResponseHelper.ok(friendLinkPage);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("system:friendLink:add")
    @Operation(summary = "Add link", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add link")
    @OperationLogger(value = "Add link")
    public EmptyResponse addFriendLink(@RequestBody BOAddFriendLinkRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        friendLinkService.create(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @SaCheckPermission("system:friendLink:update")
    @Operation(summary = "Update link", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update link")
    @OperationLogger(value = "Update link")
    public EmptyResponse update(@RequestBody BOUpdateFriendLinkRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        friendLinkService.update(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:friendLink:delete")
    @Operation(summary = "Delete Link", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete Link")
    @OperationLogger(value = "Delete Link")
    public EmptyResponse deleteFriendLink(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        friendLinkService.bulkDelete(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @SaCheckPermission("system:friendLink:top")
    @Operation(summary = "Topping link", method = "GET")
    @ApiResponse(responseCode = "200", description = "Topping link")
    @OperationLogger(value = "Topping link")
    public EmptyResponse top(@RequestParam(name = "id", required = true) String id) {
        String userId = StpUtil.getLoginIdAsString();
        friendLinkService.top(userId, id);
        return ResponseHelper.ok();
    }
}
