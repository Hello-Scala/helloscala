package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.vo.menu.RouterVO;

import java.util.List;

public interface MenuService extends IService<Menu>{
    ResponseResult selectMenuTreeList(List<Menu> list);


    ResponseResult addMenu(Menu menu);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Integer id);

    List<RouterVO> buildRouterTree(List<Menu> menus);

    ResponseResult getMenuOptions();

    List<String> selectButtonPermissions(String userId);
}
