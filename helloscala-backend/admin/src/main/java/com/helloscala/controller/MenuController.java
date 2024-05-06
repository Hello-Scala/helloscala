package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Menu;
import com.helloscala.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system/menu")
@Tag(name = "系统菜单管理-接口")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @Operation(summary = "获取菜单树", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取菜单树")
    public ResponseResult selectMenuTreeList() {
        return menuService.selectMenuTreeList(menuService.list());
    }

    @GetMapping(value = "/getMenuOptions")
    @Operation(summary = "获取下拉菜单树", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取下拉菜单树")
    public ResponseResult getMenuOptions() {
        return menuService.getMenuOptions();
    }


    @GetMapping(value = "/info/{id}")
    @Operation(summary = "菜单详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "菜单详情")
    public ResponseResult selectMenuById(@PathVariable(value = "id") Integer id) {
        return ResponseResult.success(menuService.getById(id));
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:menu:add")
    @Operation(summary = "添加菜单", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加菜单")
    @OperationLogger(value = "添加菜单")
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:menu:update")
    @Operation(summary = "修改菜单", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改菜单")
    @OperationLogger(value = "修改菜单")
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping(value = "/delete/{id}")
    @SaCheckPermission("system:menu:delete")
    @Operation(summary = "删除菜单", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除菜单")
    @OperationLogger(value = "删除菜单")
    public ResponseResult deleteMenu(@PathVariable(value = "id") Integer id) {
        return menuService.deleteMenu(id);
    }
}
