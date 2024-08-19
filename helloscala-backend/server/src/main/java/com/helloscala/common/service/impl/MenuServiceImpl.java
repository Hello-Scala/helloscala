package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.mapper.MenuMapper;
import com.helloscala.common.service.MenuService;
import com.helloscala.common.service.util.MenuHelper;
import com.helloscala.common.vo.menu.MenuOptionVO;
import com.helloscala.common.vo.menu.RouterVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public List<Menu> listMenuTree(Set<String> menuIds) {
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
    public List<Menu> listAllMenuTree() {
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
        baseMapper.delete(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, id));
    }

    @Override
    public List<RouterVO> buildRouterTree(List<Menu> menus) {
        List<RouterVO> resultList = new ArrayList<>();
        for (Menu menu : menus) {
            String parentId = menu.getParentId();
            if (parentId == null || parentId.equals("0")) {
                RouterVO.MetaVO metaVO = new RouterVO.MetaVO(menu.getTitle(), menu.getIcon(), menu.getHidden());
                RouterVO build = RouterVO.builder().id(menu.getId()).path(menu.getPath()).name(menu.getName()).component(menu.getComponent())
                        .meta(metaVO).sort(menu.getSort()).build();
                resultList.add(build);
            }
        }
        resultList.sort(Comparator.comparingInt(RouterVO::getSort));

        for (RouterVO routerVO : resultList) {
            routerVO.setChildren(getRouterChild(routerVO.getId(), menus));
        }
        return resultList;
    }

    @Override
    public List<MenuOptionVO> getMenuOptions() {
        List<Menu> menus = baseMapper.selectList(null);

        List<MenuOptionVO> options = menus.stream().filter(m -> Objects.isNull(m.getParentId()) || "0".equals(m.getParentId()))
                .map(m -> new MenuOptionVO(m.getId(), m.getTitle())).toList();

        Map<String, List<Menu>> childMenuMap = menus.stream().filter(m -> Objects.nonNull(m.getParentId())).collect(Collectors.groupingBy(Menu::getParentId));
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

    private List<RouterVO> getRouterChild(String pid, List<Menu> menus) {
        if (menus == null) {
            return Collections.emptyList();
        }
        Map<String, RouterVO> routerMap = new HashMap<>();
        for (Menu e : menus) {
            String parentId = e.getParentId();
            if (parentId != null && parentId.equals(pid)) {
                // 子菜单的下级菜单
                RouterVO.MetaVO metaVO = new RouterVO.MetaVO(e.getTitle(), e.getIcon(), e.getHidden());
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

    private List<MenuOptionVO> getOptionsChild(String pid, List<Menu> menus) {
        if (menus == null) {
            return Collections.emptyList();
        }

        Map<String, MenuOptionVO> optionsMap = new HashMap<>();
        for (Menu menu : menus) {
            String parentId = menu.getParentId();
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

}
