package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.service.FriendLinkService;
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
@RequestMapping("/system/friend")
@Tag(name = "Link management")
@RequiredArgsConstructor
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Operation(summary = "List link", method = "GET")
    @ApiResponse(responseCode = "200", description = "友链列表")
    public Response<Page<FriendLink>> selectFriendLinkPage(@RequestParam(name = "name", required = false) String name,
                                                           @RequestParam(name = "status", required = false) Integer status){
        Page<FriendLink> friendLinkPage = friendLinkService.selectFriendLinkPage(name, status);
        return ResponseHelper.ok(friendLinkPage);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:friendLink:add")
    @Operation(summary = "Add link", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add link")
    @OperationLogger(value = "Add link")
    public EmptyResponse addFriendLink(@RequestBody FriendLink friendLink){
        friendLinkService.addFriendLink(friendLink);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:friendLink:update")
    @Operation(summary = "Update link", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update link")
    @OperationLogger(value = "Update link")
    public EmptyResponse update(@RequestBody FriendLink friendLink){
        friendLinkService.updateFriendLink(friendLink);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:friendLink:delete")
    @Operation(summary = "Delete Link", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete Link")
    @OperationLogger(value = "Delete Link")
    public EmptyResponse deleteFriendLink(@RequestBody List<Integer> ids){
        friendLinkService.deleteFriendLink(ids);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/top",method = RequestMethod.GET)
    @SaCheckPermission("system:friendLink:top")
    @Operation(summary = "Topping link", method = "GET")
    @ApiResponse(responseCode = "200", description = "Topping link")
    @OperationLogger(value = "Topping link")
    public EmptyResponse top(@RequestParam(name = "id", required = true) Integer id){
        friendLinkService.top(id);
        return ResponseHelper.ok();
    }
}
