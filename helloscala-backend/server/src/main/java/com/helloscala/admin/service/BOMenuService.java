package com.helloscala.admin.service;

import com.helloscala.admin.controller.request.BOCreateMenuRequest;
import com.helloscala.admin.controller.request.BOUpdateMenuRequest;
import com.helloscala.admin.controller.view.BOMenuOptionView;
import com.helloscala.admin.controller.view.BOMenuView;
import com.helloscala.service.service.MenuService;
import com.helloscala.service.web.request.CreateMenuRequest;
import com.helloscala.service.web.request.UpdateMenuRequest;
import com.helloscala.service.web.view.MenuOptionView;
import com.helloscala.service.web.view.MenuView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOMenuService {
    private final MenuService menuService;

    public List<BOMenuView> list() {
        List<MenuView> menuViews = menuService.listAllMenuTree();
        return menuViews.stream().map(BOMenuService::toMenuView).toList();
    }

    public List<BOMenuOptionView> getMenuOptions() {
        List<MenuOptionView> menuOptions = menuService.getMenuOptions();
        return menuOptions.stream().map(BOMenuService::buildBOMenuOptionView).toList();
    }

    public BOMenuView get(String id) {
        MenuView menuView = menuService.get(id);
        return toMenuView(menuView);
    }

    public void create(String userId, BOCreateMenuRequest request) {
        CreateMenuRequest createMenuRequest = new CreateMenuRequest();
        createMenuRequest.setParentId(request.getParentId());
        createMenuRequest.setPath(request.getPath());
        createMenuRequest.setComponent(request.getComponent());
        createMenuRequest.setTitle(request.getTitle());
        createMenuRequest.setSort(request.getSort());
        createMenuRequest.setIcon(request.getIcon());
        createMenuRequest.setType(request.getType());
        createMenuRequest.setName(request.getName());
        createMenuRequest.setPerm(request.getPerm());
        createMenuRequest.setHidden(request.getHidden());
        createMenuRequest.setRequestBy(userId);
        menuService.addMenu(createMenuRequest);
    }

    public void update(String userId, BOUpdateMenuRequest request) {
        UpdateMenuRequest updateMenuRequest = new UpdateMenuRequest();
        updateMenuRequest.setId(request.getId());
        updateMenuRequest.setParentId(request.getParentId());
        updateMenuRequest.setPath(request.getPath());
        updateMenuRequest.setComponent(request.getComponent());
        updateMenuRequest.setTitle(request.getTitle());
        updateMenuRequest.setSort(request.getSort());
        updateMenuRequest.setIcon(request.getIcon());
        updateMenuRequest.setType(request.getType());
        updateMenuRequest.setName(request.getName());
        updateMenuRequest.setPerm(request.getPerm());
        updateMenuRequest.setHidden(request.getHidden());
        updateMenuRequest.setRequestBy(userId);
        menuService.updateMenu(updateMenuRequest);
    }

    public void delete(String userId, String id) {
        menuService.deleteMenu(id);
        log.info("userId={}, deleted friendLink id={}", userId, id);
    }

    private static BOMenuView toMenuView(MenuView menu) {
        BOMenuView menuView = new BOMenuView();
        menuView.setId(menu.getId());
        menuView.setParentId(menu.getParentId());
        menuView.setPath(menu.getPath());
        menuView.setComponent(menu.getComponent());
        menuView.setTitle(menu.getTitle());
        menuView.setSort(menu.getSort());
        menuView.setIcon(menu.getIcon());
        menuView.setType(menu.getType());
        menuView.setName(menu.getName());
        menuView.setPerm(menu.getPerm());
        menuView.setHidden(menu.getHidden());
        menuView.setCreatedTime(menu.getCreatedTime());
        menuView.setUpdateTime(menu.getUpdateTime());
        menuView.setChildren(menu.getChildren().stream().map(BOMenuService::toMenuView).toList());
        return menuView;
    }

    private static BOMenuOptionView buildBOMenuOptionView(MenuOptionView menuOption) {
        BOMenuOptionView menuOptionView = new BOMenuOptionView();
        menuOptionView.setMenuId(menuOption.getMenuId());
        menuOptionView.setLabel(menuOptionView.getLabel());
        menuOptionView.setChildren(menuOption.getChildren().stream().map(BOMenuService::buildBOMenuOptionView).toList());
        return menuOptionView;
    }
}
