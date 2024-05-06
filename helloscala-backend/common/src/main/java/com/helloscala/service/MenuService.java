package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Menu;
import com.helloscala.vo.menu.RouterVO;

import java.util.List;

public interface MenuService extends IService<Menu>{

    /**
     * 获取所有菜单 并组装成树形结构
     * @param list
     * @return
     */
    ResponseResult selectMenuTreeList(List<Menu> list);


    /**
     * 添加
     * @param menu
     * @return
     */
    ResponseResult addMenu(Menu menu);

    /**
     * 修改
     * @param menu
     * @return
     */
    ResponseResult updateMenu(Menu menu);

    /**
     * 删除
     * @param id
     * @return
     */
    ResponseResult deleteMenu(Integer id);

    /**
     * 组件路由菜单树
     * @param menus
     * @return
     */
    List<RouterVO> buildRouterTree(List<Menu> menus);

    /**
     * 获取子树
     * @return
     */
    ResponseResult getMenuOptions();


    /**
     * 获取用户权限集合
     * @param userId
     * @return
     */
    List<String> selectButtonPermissions(String userId);
}
