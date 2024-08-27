package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Menu;
import com.helloscala.common.vo.menu.MenuOptionVO;
import com.helloscala.common.vo.menu.RouterVO;

import java.util.List;
import java.util.Set;

public interface MenuService extends IService<Menu>{
    List<Menu> listMenuTree(Set<String> menuIds);

    List<Menu> listAllMenuTree();

    List<String> listAllPerms();

    List<String> listMenuPerms(Set<String> menuIds);

    void addMenu(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenu(Integer id);

    List<RouterVO> buildRouterTree(List<Menu> menus);

    List<MenuOptionVO> getMenuOptions();

    List<String> selectButtonPermissions(String userId);
}
