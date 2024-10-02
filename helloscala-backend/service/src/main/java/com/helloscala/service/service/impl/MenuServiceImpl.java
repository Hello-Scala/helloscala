package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Menu;
import com.helloscala.service.mapper.MenuMapper;
import com.helloscala.service.service.MenuService;
import com.helloscala.service.service.util.MenuHelper;
import com.helloscala.service.web.request.CreateMenuRequest;
import com.helloscala.service.web.request.UpdateMenuRequest;
import com.helloscala.service.web.view.MenuOptionView;
import com.helloscala.service.web.view.MenuView;
import com.helloscala.service.web.view.RouteView;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public MenuView get(String id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, id)
                .or()
                .eq(Menu::getParentId, id);
        List<Menu> menus = baseMapper.selectList(queryWrapper);
        Optional<Menu> menuOptional = menus.stream().filter(menu -> id.equals(menu.getId())).findFirst();
        if (menuOptional.isEmpty()) {
            return null;
        }
        List<Menu> children = menus.stream().filter(menu -> id.equals(menu.getParentId())).toList();
        List<MenuView> childViews = MenuHelper.toMenusWithChildren(children);

        MenuView menuView = MenuHelper.toMenuView(menuOptional.get());
        menuView.setChildren(childViews);
        return menuView;
    }

    @Override
    public List<MenuView> listMenuTree(Set<String> menuIds) {
        if (ObjectUtils.isEmpty(menuIds)) {
            return List.of();
        }

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getId, menuIds)
                .or()
                .in(Menu::getParentId, menuIds);
        List<Menu> menus = baseMapper.selectList(queryWrapper);
        return MenuHelper.toMenusWithChildren(menus);
    }

    @Override
    public List<MenuView> listAllMenuTree() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getSort);
        List<Menu> menus = list(queryWrapper);
        return MenuHelper.toMenusWithChildren(menus);
    }

    @Override
    public List<String> listAllPerms() {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.lambda().select(Menu::getPerm);
        List<Menu> menus = baseMapper.selectList(menuQueryWrapper);
        return toPerms(menus);
    }

    @Override
    public List<String> listMenuPerms(Set<String> menuIds) {
        if (ObjectUtils.isEmpty(menuIds)) {
            return List.of();
        }

        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.lambda().select(Menu::getPerm).in(Menu::getId, menuIds);
        List<Menu> menus = baseMapper.selectList(menuQueryWrapper);
        return toPerms(menus);
    }

    @NotNull
    private static List<String> toPerms(List<Menu> menus) {
        return menus.stream()
                .filter(Objects::nonNull)
                .map(Menu::getPerm).distinct().toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMenu(CreateMenuRequest request) {
        if (request.getType().equals("CATALOG")) {
            request.setComponent("Layout");
        }

        Menu menu = new Menu();
        menu.setParentId(request.getParentId());
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setTitle(request.getTitle());
        menu.setSort(request.getSort());
        menu.setIcon(request.getIcon());
        menu.setType(request.getType());
        menu.setName(request.getName());
        menu.setPerm(request.getPerm());
        menu.setHidden(request.getHidden());
        menu.setCreatedTime(new Date());
        baseMapper.insert(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(UpdateMenuRequest request) {
        Menu menu = baseMapper.selectById(request.getId());
        if (Objects.isNull(menu)) {
            throw new NotFoundException("Menu not found, id={}!", request.getId());
        }
        menu.setParentId(request.getParentId());
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setTitle(request.getTitle());
        menu.setSort(request.getSort());
        menu.setIcon(request.getIcon());
        menu.setType(request.getType());
        menu.setName(request.getName());
        menu.setPerm(request.getPerm());
        menu.setHidden(request.getHidden());
        menu.setUpdateTime(new Date());
        baseMapper.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(String id) {
        baseMapper.deleteById(id);
        baseMapper.delete(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, id));
    }

    @Override
    public List<RouteView> buildRouterTree(List<Menu> menus) {
        List<RouteView> resultList = new ArrayList<>();
        for (Menu menu : menus) {
            String parentId = menu.getParentId();
            if (parentId == null || parentId.equals("0")) {
                RouteView routeView = buildRouteView(menu);
                resultList.add(routeView);
            }
        }
        resultList.sort(Comparator.comparingInt(RouteView::getSort));

        for (RouteView routerVO : resultList) {
            routerVO.setChildren(getRouterChild(routerVO.getId(), menus));
        }
        return resultList;
    }

    private static @NotNull RouteView buildRouteView(Menu menu) {
        RouteView.MetaView metaVO = new RouteView.MetaView(menu.getTitle(), menu.getIcon(), menu.getHidden());
        RouteView routeView = new RouteView();
        routeView.setId(menu.getId());
        routeView.setPath(menu.getPath());
        routeView.setName(menu.getName());
        routeView.setComponent(menu.getComponent());
        routeView.setMeta(metaVO);
        routeView.setSort(menu.getSort());
        return routeView;
    }

    @Override
    public List<MenuOptionView> getMenuOptions() {
        List<Menu> menus = baseMapper.selectList(null);

        List<MenuOptionView> options = menus.stream().filter(m -> Objects.isNull(m.getParentId()) || "0".equals(m.getParentId()))
                .map(m -> new MenuOptionView(m.getId(), m.getTitle())).toList();

        Map<String, List<Menu>> childMenuMap = menus.stream().filter(m -> Objects.nonNull(m.getParentId())).collect(Collectors.groupingBy(Menu::getParentId));
        options.forEach(m -> {
            List<MenuOptionView> menuOptions = Optional.ofNullable(childMenuMap.get(m.getMenuId())).map(ls -> ls.stream().map(menu -> new MenuOptionView(menu.getId(), menu.getTitle())).toList()).orElse(new ArrayList<>());
            m.setChildren(menuOptions);
        });
        return options;
    }

    @Override
    public List<String> selectButtonPermissions(String userId) {
        return baseMapper.selectButtonPermissions(userId, StpUtil.hasRole(Constants.ADMIN_CODE));
    }

    private List<RouteView> getRouterChild(String pid, List<Menu> menus) {
        if (menus == null) {
            return Collections.emptyList();
        }
        Map<String, RouteView> routerMap = new HashMap<>();
        for (Menu e : menus) {
            String parentId = e.getParentId();
            if (parentId != null && parentId.equals(pid)) {
                // 子菜单的下级菜单
                RouteView.MetaView metaVO = new RouteView.MetaView(e.getTitle(), e.getIcon(), e.getHidden());
                RouteView routeView = buildRouteView(e);
                routerMap.put(e.getId(), routeView);
            }
        }

        List<RouteView> children = new ArrayList<>(routerMap.values());
        children.sort(Comparator.comparingInt(RouteView::getSort));
        for (RouteView e : children) {
            e.setChildren(getRouterChild(e.getId(), menus));
        }
        return children.isEmpty() ? Collections.emptyList() : children;
    }

    private List<MenuOptionView> getOptionsChild(String pid, List<Menu> menus) {
        if (menus == null) {
            return Collections.emptyList();
        }

        Map<String, MenuOptionView> optionsMap = new HashMap<>();
        for (Menu menu : menus) {
            String parentId = menu.getParentId();
            if (parentId != null && parentId.equals(pid)) {
                MenuOptionView menuOptionsVO = new MenuOptionView(menu.getId(), menu.getTitle());
                optionsMap.put(menu.getId(), menuOptionsVO);
            }
        }

        List<MenuOptionView> childrens = new ArrayList<>(optionsMap.values());

        for (MenuOptionView e : childrens) {
            e.setChildren(getOptionsChild(e.getMenuId(), menus));
        }
        return childrens.isEmpty() ? Collections.emptyList() : childrens;
    }

}
