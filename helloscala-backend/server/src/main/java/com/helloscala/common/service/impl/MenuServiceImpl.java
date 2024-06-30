package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.mapper.MenuMapper;
import com.helloscala.common.service.MenuService;
import com.helloscala.common.vo.menu.MenuOptionVO;
import com.helloscala.common.vo.menu.RouterVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> selectMenuTreeList() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getSort);
        List<Menu> menus = list(queryWrapper);

        Map<Integer, List<Menu>> childMenuMap = menus.stream().filter(m -> Objects.nonNull(m.getParentId())).collect(Collectors.groupingBy(Menu::getParentId));
        menus.forEach(m -> m.setChildren(childMenuMap.get(m.getId())));
        return menus;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMenu(Menu menu) {
        if (menu.getType().equals("CATALOG")) {
            menu.setComponent("Layout");
        }
        baseMapper.insert(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Menu menu) {
        baseMapper.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Integer id) {
        baseMapper.deleteById(id);
        baseMapper.delete(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId,id));
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
    public List<MenuOptionVO> getMenuOptions() {
        List<Menu> menus = baseMapper.selectList(null);

        List<MenuOptionVO> options = menus.stream().filter(m -> Objects.isNull(m.getParentId()) || 0 == m.getParentId())
                .map(m -> new MenuOptionVO(m.getId(), m.getTitle())).toList();

        Map<Integer, List<Menu>> childMenuMap = menus.stream().filter(m -> Objects.nonNull(m.getParentId())).collect(Collectors.groupingBy(Menu::getParentId));
        options.forEach(m -> {
            List<MenuOptionVO> menuOptions = Optional.ofNullable(childMenuMap.get(m.getValue())).map(ls -> ls.stream().map(menu -> new MenuOptionVO(menu.getId(), menu.getTitle())).toList()).orElse(new ArrayList<>());
            m.setChildren(menuOptions);
        });
        return options;
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

    private List<MenuOptionVO> getOptionsChild(Integer pid , List<Menu> menus){
        if (menus == null) {
            return Collections.emptyList();
        }

        Map<Integer, MenuOptionVO> optionsMap = new HashMap<>();
        for (Menu menu : menus) {
            Integer parentId = menu.getParentId();
            if (parentId != null && parentId.equals(pid)) {
                MenuOptionVO menuOptionsVO = new MenuOptionVO(menu.getId(), menu.getTitle());
                optionsMap.put(menu.getId(), menuOptionsVO);
            }
        }

        List<MenuOptionVO> childrens = new ArrayList<>(optionsMap.values());

        for (MenuOptionVO e : childrens) {
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
