package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.role.RoleMenuDTO;
import com.helloscala.common.entity.Role;
import com.helloscala.common.service.RoleService;
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
    public ResponseResult selectRolePage(@RequestParam(name = "name", required = false) String name) {
        return roleService.selectRolePage(name);
    }


    @RequestMapping(value = "queryUserRole", method = RequestMethod.GET)
    @Operation(summary = "Get user role", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get user role")
    public ResponseResult getCurrentUserRole() {
        return roleService.getCurrentUserRole();
    }


    @RequestMapping(value = "getRoleMenuIds", method = RequestMethod.GET)
    @Operation(summary = "Get role menu ids", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get role menu ids")
    public ResponseResult selectRoleMenuById(@RequestParam(name = "roleId", required = true) Integer roleId) {
        return roleService.selectRoleMenuById(roleId);
    }

    @SaCheckPermission("system:role:assign")
    @RequestMapping(value = "updateRoleMenus", method = RequestMethod.PUT)
    @Operation(summary = "Assign role menu", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Assign role menu")
    public ResponseResult assignRoleMenus(@RequestBody RoleMenuDTO roleMenuDTO) {
        return roleService.assignRoleMenus(roleMenuDTO);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @SaCheckPermission("system:role:add")
    @Operation(summary = "Add role", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add role")
    @OperationLogger(value = "Add role")
    public ResponseResult addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @SaCheckPermission("system:role:update")
    @Operation(summary = "Update role", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update role")
    @OperationLogger(value = "Update role")
    public ResponseResult updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:role:delete")
    @Operation(summary = "Delete role", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete role")
    @OperationLogger(value = "Delete role")
    public ResponseResult deleteRole(@RequestBody List<Integer> ids) {
        return roleService.deleteRole(ids);
    }
}
