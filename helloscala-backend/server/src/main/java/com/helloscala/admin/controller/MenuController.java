package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.helloscala.admin.controller.request.BOCreateMenuRequest;
import com.helloscala.admin.controller.request.BOUpdateMenuRequest;
import com.helloscala.admin.controller.view.BOMenuOptionView;
import com.helloscala.admin.controller.view.BOMenuView;
import com.helloscala.admin.service.BOMenuService;
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
@RequestMapping("/system/menu")
@Tag(name = "System menu management")
@RequiredArgsConstructor
public class MenuController {

    private final BOMenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @Operation(summary = "Get menu tree", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取菜单树")
    public Response<List<BOMenuView>> list() {
        List<BOMenuView> menus = menuService.list();
        return ResponseHelper.ok(menus);
    }

    @GetMapping(value = "/getMenuOptions")
    @Operation(summary = "Get menu options", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get menu options")
    public Response<List<BOMenuOptionView>> getMenuOptions() {
        List<BOMenuOptionView> menuOptions = menuService.getMenuOptions();
        return ResponseHelper.ok(menuOptions);
    }


    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get menu detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get menu detail")
    public Response<BOMenuView> get(@PathVariable(value = "id") String id) {
        BOMenuView menuView = menuService.get(id);
        return ResponseHelper.ok(menuView);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:menu:add")
    @Operation(summary = "Add menue", method = "POST")
    @ApiResponse(responseCode = "200", description = "Add menue")
    @OperationLogger(value = "Add menue")
    public EmptyResponse create(@RequestBody BOCreateMenuRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        menuService.create(userId, request);
        return ResponseHelper.ok();
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:menu:update")
    @Operation(summary = "Update menu", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update menu")
    @OperationLogger(value = "Update menu")
    public EmptyResponse update(@RequestBody BOUpdateMenuRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        menuService.update(userId, request);
        return ResponseHelper.ok();
    }

    @DeleteMapping(value = "/delete/{id}")
    @SaCheckPermission("system:menu:delete")
    @Operation(summary = "Delte menu", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delte menu")
    @OperationLogger(value = "Delte menu")
    public EmptyResponse delete(@PathVariable(value = "id") String id) {
        String userId = StpUtil.getLoginIdAsString();
        menuService.delete(userId, id);
        return ResponseHelper.ok();
    }
}
