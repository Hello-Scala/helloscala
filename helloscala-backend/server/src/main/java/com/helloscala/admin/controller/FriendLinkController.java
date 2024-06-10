package com.helloscala.admin.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.service.FriendLinkService;
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
    public ResponseResult selectFriendLinkPage(@RequestParam(name = "name", required = false) String name,
                                               @RequestParam(name = "status", required = false) Integer status){
        return friendLinkService.selectFriendLinkPage(name,status);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("system:friendLink:add")
    @Operation(summary = "Add link", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add link")
    @OperationLogger(value = "Add link")
    public ResponseResult addFriendLink(@RequestBody FriendLink friendLink){
        return friendLinkService.addFriendLink(friendLink);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @SaCheckPermission("system:friendLink:update")
    @Operation(summary = "Update link", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update link")
    @OperationLogger(value = "Update link")
    public ResponseResult update(@RequestBody FriendLink friendLink){
        return friendLinkService.updateFriendLink(friendLink);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("system:friendLink:delete")
    @Operation(summary = "Delete Link", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete Link")
    @OperationLogger(value = "Delete Link")
    public ResponseResult deleteFriendLink(@RequestBody List<Integer> ids){
        return friendLinkService.deleteFriendLink(ids);
    }

    @RequestMapping(value = "/top",method = RequestMethod.GET)
    @SaCheckPermission("system:friendLink:top")
    @Operation(summary = "Topping link", method = "GET")
    @ApiResponse(responseCode = "200", description = "Topping link")
    @OperationLogger(value = "Topping link")
    public ResponseResult top(@RequestParam(name = "id", required = true) Integer id){
        return friendLinkService.top(id);
    }
}

