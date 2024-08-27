package com.helloscala.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.service.entity.Menu;
import com.helloscala.service.entity.RoleMenu;
import com.helloscala.service.mapper.MenuMapper;
import com.helloscala.service.mapper.RoleMenuMapper;
import com.helloscala.service.service.MenuService;
import com.helloscala.service.service.RoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Steve Zou
 */
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl implements RoleMenuService {
    private final MenuService menuService;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> listAllMenus() {
        return menuService.listAllMenuTree();
    }

    @Override
    public List<Menu> listByRoleId(String roleId) {
        if (StrUtil.isBlank(roleId)) {
            return List.of();
        }

        LambdaQueryWrapper<RoleMenu> roleMenuQuery = new LambdaQueryWrapper<>();
        roleMenuQuery.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(roleMenuQuery);
        if (ObjectUtils.isEmpty(roleMenus)) {
            return List.of();
        }

        Set<String> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
        return menuService.listMenuTree(menuIds);
    }

    @Override
    public List<String> listAllPerms() {
        return null;
    }

    @Override
    public List<String> listRolePerms(String roleId) {
        LambdaQueryWrapper<RoleMenu> roleMenuQuery = new LambdaQueryWrapper<>();
        roleMenuQuery.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(roleMenuQuery);
        Set<String> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
        return menuService.listMenuPerms(menuIds);
    }
}
