package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.vo.menu.MenuOptionVO;
import com.helloscala.common.vo.menu.RouterVO;

import java.util.List;

public interface MenuService extends IService<Menu>{
    List<Menu> selectMenuTreeList();


    void addMenu(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenu(Integer id);

    List<RouterVO> buildRouterTree(List<Menu> menus);

    List<MenuOptionVO> getMenuOptions();

    List<String> selectButtonPermissions(String userId);
}
