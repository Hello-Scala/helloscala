package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/system/menu")
@Tag(name = "System menu management")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @Operation(summary = "Get menu tree", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取菜单树")
    public ResponseResult selectMenuTreeList() {
        return menuService.selectMenuTreeList(menuService.list());
    }

    @GetMapping(value = "/getMenuOptions")
    @Operation(summary = "Get menu options", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get menu options")
    public ResponseResult getMenuOptions() {
        return menuService.getMenuOptions();
    }


    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get menu detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get menu detail")
    public ResponseResult selectMenuById(@PathVariable(value = "id") Integer id) {
        return ResponseResult.success(menuService.getById(id));
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:menu:add")
    @Operation(summary = "Add menue", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add menue")
    @OperationLogger(value = "Add menue")
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:menu:update")
    @Operation(summary = "Update menu", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update menu")
    @OperationLogger(value = "Update menu")
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping(value = "/delete/{id}")
    @SaCheckPermission("system:menu:delete")
    @Operation(summary = "Delte menu", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delte menu")
    @OperationLogger(value = "Delte menu")
    public ResponseResult deleteMenu(@PathVariable(value = "id") Integer id) {
        return menuService.deleteMenu(id);
    }
}
