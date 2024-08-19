package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.service.MenuService;
import com.helloscala.common.vo.menu.MenuOptionVO;
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
@RequestMapping("/system/menu")
@Tag(name = "System menu management")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @Operation(summary = "Get menu tree", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取菜单树")
    public Response<List<Menu>> selectMenuTreeList() {
        List<Menu> menus = menuService.listAllMenuTree();
        return ResponseHelper.ok(menus);
    }

    @GetMapping(value = "/getMenuOptions")
    @Operation(summary = "Get menu options", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get menu options")
    public Response<List<MenuOptionVO>> getMenuOptions() {
        List<MenuOptionVO> menuOptions = menuService.getMenuOptions();
        return ResponseHelper.ok(menuOptions);
    }


    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get menu detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get menu detail")
    public Response<Menu> selectMenuById(@PathVariable(value = "id") Integer id) {
        Menu menu = menuService.getById(id);
        return ResponseHelper.ok(menu);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:menu:add")
    @Operation(summary = "Add menue", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add menue")
    @OperationLogger(value = "Add menue")
    public EmptyResponse addMenu(@RequestBody Menu menu) {
        menuService.addMenu(menu);
        return ResponseHelper.ok();
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:menu:update")
    @Operation(summary = "Update menu", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update menu")
    @OperationLogger(value = "Update menu")
    public EmptyResponse updateMenu(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return ResponseHelper.ok();
    }

    @DeleteMapping(value = "/delete/{id}")
    @SaCheckPermission("system:menu:delete")
    @Operation(summary = "Delte menu", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delte menu")
    @OperationLogger(value = "Delte menu")
    public EmptyResponse deleteMenu(@PathVariable(value = "id") Integer id) {
        menuService.deleteMenu(id);
        return ResponseHelper.ok();
    }
}
