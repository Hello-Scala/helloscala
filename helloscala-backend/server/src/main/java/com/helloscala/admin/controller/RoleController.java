package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOAssignRoleMenuRequest;
import com.helloscala.admin.controller.request.BOCreateRoleRequest;
import com.helloscala.admin.controller.request.BOUpdateRoleRequest;
import com.helloscala.admin.controller.view.BORoleView;
import com.helloscala.admin.service.BORoleService;
import com.helloscala.common.annotation.OperationLogger;
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
@RequestMapping("/system/role")
@Tag(name = "Role management")
@RequiredArgsConstructor
public class RoleController {
    private final BORoleService roleService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    @Operation(summary = "List roles", method = "GET")
    @ApiResponse(responseCode = "200", description = "List roles")
    public Response<Page<BORoleView>> listByPage(@RequestParam(name = "name", required = false) String name) {
        Page<BORoleView> rolePage = roleService.listByPage(name);
        return ResponseHelper.ok(rolePage);
    }


    @RequestMapping(value = "queryUserRole", method = RequestMethod.GET)
    @Operation(summary = "Get user role", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user role")
    public Response<List<String>> getCurrentUserRoleMenuIds() {
        String userId = StpUtil.getLoginIdAsString();
        List<String> currentUserRole = roleService.getUserRoleMenuIds(userId);
        return ResponseHelper.ok(currentUserRole);
    }


    @RequestMapping(value = "getRoleMenuIds", method = RequestMethod.GET)
    @Operation(summary = "Get role menu ids", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get role menu ids")
    public Response<List<String>> listRoleMenuById(@RequestParam(name = "roleId", required = true) String roleId) {
        List<String> menuIds = roleService.listRoleMenuById(roleId);
        return ResponseHelper.ok(menuIds);
    }

    @SaCheckPermission("system:role:assign")
    @RequestMapping(value = "updateRoleMenus", method = RequestMethod.PUT)
    @Operation(summary = "Assign role menu", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Assign role menu")
    public EmptyResponse assignRoleMenus(@RequestBody BOAssignRoleMenuRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        roleService.assignRoleMenus(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @SaCheckPermission("system:role:add")
    @Operation(summary = "Add role", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add role")
    @OperationLogger(value = "Add role")
    public EmptyResponse create(@RequestBody BOCreateRoleRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        roleService.create(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @SaCheckPermission("system:role:update")
    @Operation(summary = "Update role", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update role")
    @OperationLogger(value = "Update role")
    public EmptyResponse update(@RequestBody BOUpdateRoleRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        roleService.update(userId, request);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:role:delete")
    @Operation(summary = "Delete role", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete role")
    @OperationLogger(value = "Delete role")
    public EmptyResponse deleteRole(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        roleService.delete(userId, ids);
        return ResponseHelper.ok();
    }
}
