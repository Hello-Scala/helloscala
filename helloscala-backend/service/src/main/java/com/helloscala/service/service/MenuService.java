package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Menu;
import com.helloscala.service.web.request.CreateMenuRequest;
import com.helloscala.service.web.request.UpdateMenuRequest;
import com.helloscala.service.web.view.MenuOptionView;
import com.helloscala.service.web.view.MenuView;
import com.helloscala.service.web.view.RouteView;

import java.util.List;
import java.util.Set;

public interface MenuService extends IService<Menu>{
    MenuView get(String id);

    List<MenuView> listMenuTree(Set<String> menuIds);

    List<MenuView> listAllMenuTree();

    List<String> listAllPerms();

    List<String> listMenuPerms(Set<String> menuIds);

    void addMenu(CreateMenuRequest request);

    void updateMenu(UpdateMenuRequest request);

    void deleteMenu(String id);

    List<RouteView> buildRouterTree(List<Menu> menus);

    List<MenuOptionView> getMenuOptions();

    List<String> selectButtonPermissions(String userId);
}
