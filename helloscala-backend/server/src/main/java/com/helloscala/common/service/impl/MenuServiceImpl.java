package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.mapper.MenuMapper;
import com.helloscala.common.service.MenuService;
import com.helloscala.common.vo.menu.MenuOptionsVO;
import com.helloscala.common.vo.menu.RouterVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public ResponseResult selectMenuTreeList(List<Menu> list) {
        List<Menu> resultList = new ArrayList<>();
        for (Menu menu : list) {
            Integer parentId = menu.getParentId();
            if ( parentId == null || parentId == 0)
                resultList.add(menu);
        }
        for (Menu menu : resultList) {
            menu.setChildren(getMenTreeChild(menu.getId(),list));
        }
        resultList.sort(Comparator.comparingInt(Menu::getSort));
        return ResponseResult.success(resultList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addMenu(Menu menu) {
        if (menu.getType().equals("CATALOG")) {
            menu.setComponent("Layout");
        }
        baseMapper.insert(menu);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateMenu(Menu menu) {
        baseMapper.updateById(menu);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMenu(Integer id) {
        baseMapper.deleteById(id);
        baseMapper.delete(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId,id));
        return ResponseResult.success();
    }

    @Override
    public List<RouterVO> buildRouterTree(List<Menu> menus) {
        List<RouterVO> resultList = new ArrayList<>();
        for (Menu menu : menus) {
            Integer parentId = menu.getParentId();
            if ( parentId == null || parentId == 0) {
                RouterVO.MetaVO metaVO = new RouterVO.MetaVO(menu.getTitle(),menu.getIcon(),menu.getHidden());
                RouterVO build = RouterVO.builder().id(menu.getId()).path(menu.getPath()).name(menu.getName()).component(menu.getComponent())
                        .meta(metaVO).sort(menu.getSort()).build();
                resultList.add(build);
            }
        }
        resultList.sort(Comparator.comparingInt(RouterVO::getSort));

        for (RouterVO routerVO : resultList) {
            routerVO.setChildren(getRouterChild(routerVO.getId(),menus));
        }
        return resultList;
    }

    @Override
    public ResponseResult getMenuOptions() {

        List<Menu> list = baseMapper.selectList(null);
        List<MenuOptionsVO> resultList = new ArrayList<>();
        for (Menu menu : list) {
            Integer parentId = menu.getParentId();
            if ( parentId == null || parentId == 0) {
                MenuOptionsVO menuOptionsVO = new MenuOptionsVO(menu.getId(), menu.getTitle());
                resultList.add(menuOptionsVO);
            }
        }
        for (MenuOptionsVO menu : resultList) {
            menu.setChildren(getOptionsChild(menu.getValue(),list));
        }
        return ResponseResult.success(resultList);
    }

    @Override
    public List<String> selectButtonPermissions(String userId) {
        return baseMapper.selectButtonPermissions(userId, StpUtil.hasRole(Constants.ADMIN_CODE));
    }

    private List<RouterVO> getRouterChild(Integer pid , List<Menu> menus){
        if (menus == null) {
            return Collections.emptyList();
        }
        Map<Integer, RouterVO> routerMap = new HashMap<>();
        for (Menu e: menus) {
            Integer parentId = e.getParentId();
            if(parentId != null && parentId.equals(pid)){
                // 子菜单的下级菜单
                RouterVO.MetaVO metaVO = new RouterVO.MetaVO(e.getTitle(),e.getIcon(),e.getHidden());
                RouterVO build = RouterVO.builder().id(e.getId()).path(e.getPath()).name(e.getName()).component(e.getComponent())
                        .meta(metaVO).sort(e.getSort()).build();
                routerMap.put(e.getId(), build);
            }
        }

        List<RouterVO> childrens = new ArrayList<>(routerMap.values());
        childrens.sort(Comparator.comparingInt(RouterVO::getSort));

        for (RouterVO e : childrens) {
            e.setChildren(getRouterChild(e.getId(), menus));
        }

        return childrens.isEmpty() ? Collections.emptyList() : childrens;
    }

    private List<MenuOptionsVO> getOptionsChild(Integer pid , List<Menu> menus){
        if (menus == null) {
            return Collections.emptyList();
        }

        Map<Integer, MenuOptionsVO> optionsMap = new HashMap<>();
        for (Menu menu : menus) {
            Integer parentId = menu.getParentId();
            if (parentId != null && parentId.equals(pid)) {
                MenuOptionsVO menuOptionsVO = new MenuOptionsVO(menu.getId(), menu.getTitle());
                optionsMap.put(menu.getId(), menuOptionsVO);
            }
        }

        List<MenuOptionsVO> childrens = new ArrayList<>(optionsMap.values());

        for (MenuOptionsVO e : childrens) {
            e.setChildren(getOptionsChild(e.getValue(), menus));
        }
        return childrens.isEmpty() ? Collections.emptyList() : childrens;
    }

    private List<Menu> getMenTreeChild(Integer pid , List<Menu> menus){
        List<Menu> childrens = new ArrayList<>();
        for (Menu e: menus) {
            Integer parentId = e.getParentId();
            if(parentId != null && parentId.equals(pid)){
                childrens.add( e );
            }
        }
        for (Menu e: childrens) {
            e.setChildren(getMenTreeChild(e.getId(),menus));
        }
        if(childrens.isEmpty()){
            return new ArrayList<>();
        }
        return childrens;
    }
}
