package com.helloscala.service.service.util;

import com.helloscala.common.utils.ListHelper;
import com.helloscala.service.entity.Menu;
import com.helloscala.service.web.view.MenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Steve Zou
 */
public final class MenuHelper {
    public static List<MenuView> toMenusWithChildren(List<Menu> flattenMenus) {
        List<Menu> result = new ArrayList<>(flattenMenus);
        Map<String, List<Menu>> childMenuMap = result.stream().filter(m -> Objects.nonNull(m.getParentId()))
                .collect(Collectors.groupingBy(Menu::getParentId));
        return result.stream().map(m -> {
            MenuView menuView = toMenuView(m);
            menuView.setChildren(ListHelper.ofNullable(childMenuMap.get(m.getId())).stream().map(MenuHelper::toMenuView).toList());
            return menuView;
        }).toList();
    }

    public static MenuView toMenuView(Menu menu) {
        MenuView menuView = new MenuView();
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
        return menuView;
    }
}
