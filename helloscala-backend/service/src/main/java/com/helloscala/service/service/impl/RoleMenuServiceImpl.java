package com.helloscala.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.service.entity.Menu;
import com.helloscala.service.entity.Role;
import com.helloscala.service.entity.RoleMenu;
import com.helloscala.service.mapper.RoleMapper;
import com.helloscala.service.mapper.RoleMenuMapper;
import com.helloscala.service.service.MenuService;
import com.helloscala.service.service.RoleMenuService;
import com.helloscala.service.web.request.AssignRoleMenuRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Steve Zou
 */
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    private final RoleMapper roleMapper;
    private final MenuService menuService;

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
        List<RoleMenu> roleMenus = baseMapper.selectList(roleMenuQuery);
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

    @Override
    public void assignRoleMenus(AssignRoleMenuRequest request) {
        Role role = roleMapper.selectById(request.getRoleId());
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return;
        }

        LambdaQueryWrapper<RoleMenu> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.eq(RoleMenu::getRoleId, role.getId());
        baseMapper.delete(roleMenuQueryWrapper);

        List<RoleMenu> roleMenus = request.getMenuIds().stream().map(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(request.getRoleId());
            roleMenu.setMenuId(menuId);
            roleMenu.setCreatedTime(new Date());
            return roleMenu;
        }).toList();
        if (roleMenus.isEmpty()) {
            return;
        }
        saveBatch(roleMenus);
    }
}
