package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.dto.role.RoleMenuDTO;
import com.helloscala.common.entity.Role;
import com.helloscala.common.service.RoleService;
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

    private final RoleService roleService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    @Operation(summary = "List roles", method = "GET")
    @ApiResponse(responseCode = "200", description = "List roles")
    public Response<Page<Role>> selectRolePage(@RequestParam(name = "name", required = false) String name) {
        Page<Role> rolePage = roleService.selectRolePage(name);
        return ResponseHelper.ok(rolePage);
    }


    @RequestMapping(value = "queryUserRole", method = RequestMethod.GET)
    @Operation(summary = "Get user role", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user role")
    public Response<List<String>> getCurrentUserRole() {
        List<String> currentUserRole = roleService.getCurrentUserRole();
        return ResponseHelper.ok(currentUserRole);
    }


    @RequestMapping(value = "getRoleMenuIds", method = RequestMethod.GET)
    @Operation(summary = "Get role menu ids", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get role menu ids")
    public Response<List<String>> selectRoleMenuById(@RequestParam(name = "roleId", required = true) String roleId) {
        List<String> menuIds = roleService.selectRoleMenuById(roleId);
        return ResponseHelper.ok(menuIds);
    }

    @SaCheckPermission("system:role:assign")
    @RequestMapping(value = "updateRoleMenus", method = RequestMethod.PUT)
    @Operation(summary = "Assign role menu", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Assign role menu")
    public EmptyResponse assignRoleMenus(@RequestBody RoleMenuDTO roleMenuDTO) {
        roleService.assignRoleMenus(roleMenuDTO);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @SaCheckPermission("system:role:add")
    @Operation(summary = "Add role", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add role")
    @OperationLogger(value = "Add role")
    public EmptyResponse addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @SaCheckPermission("system:role:update")
    @Operation(summary = "Update role", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update role")
    @OperationLogger(value = "Update role")
    public EmptyResponse updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:role:delete")
    @Operation(summary = "Delete role", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete role")
    @OperationLogger(value = "Delete role")
    public EmptyResponse deleteRole(@RequestBody List<String> ids) {
        roleService.deleteRole(ids);
        return ResponseHelper.ok();
    }
}
