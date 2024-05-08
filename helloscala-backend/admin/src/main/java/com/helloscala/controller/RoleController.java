package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.role.RoleMenuDTO;
import com.helloscala.entity.Role;
import com.helloscala.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/system/role")
@Tag(name = "角色管理-接口")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    @Operation(summary = "角色列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "角色列表")
    public ResponseResult selectRolePage(@RequestParam(name = "name", required = false) String name) {
        return roleService.selectRolePage(name);
    }


    @RequestMapping(value = "queryUserRole", method = RequestMethod.GET)
    @Operation(summary = "获取当前登录用户所拥有的权限", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取当前登录用户所拥有的权限")
    public ResponseResult getCurrentUserRole() {
        return roleService.getCurrentUserRole();
    }


    @RequestMapping(value = "getRoleMenuIds", method = RequestMethod.GET)
    @Operation(summary = "获取该角色所有的权限", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取该角色所有的权限")
    public ResponseResult selectRoleMenuById(@RequestParam(name = "roleId", required = true) Integer roleId) {
        return roleService.selectRoleMenuById(roleId);
    }

    @SaCheckPermission("system:role:assign")
    @RequestMapping(value = "updateRoleMenus", method = RequestMethod.PUT)
    @Operation(summary = "分配角色权限", method = "PUT")
    @ApiResponse(responseCode = "200", description = "分配角色权限")
    public ResponseResult assignRoleMenus(@RequestBody RoleMenuDTO roleMenuDTO) {
        return roleService.assignRoleMenus(roleMenuDTO);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @SaCheckPermission("system:role:add")
    @Operation(summary = "添加角色", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加角色")
    @OperationLogger(value = "添加角色")
    public ResponseResult addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @SaCheckPermission("system:role:update")
    @Operation(summary = "修改角色", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改角色")
    @OperationLogger(value = "修改角色")
    public ResponseResult updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @SaCheckPermission("system:role:delete")
    @Operation(summary = "删除角色", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除角色")
    @OperationLogger(value = "删除角色")
    public ResponseResult deleteRole(@RequestBody List<Integer> ids) {
        return roleService.deleteRole(ids);
    }


}
