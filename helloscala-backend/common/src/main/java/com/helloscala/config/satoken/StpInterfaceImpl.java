package com.helloscala.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helloscala.entity.Menu;
import com.helloscala.entity.Role;
import com.helloscala.entity.RoleMenu;
import com.helloscala.entity.User;
import com.helloscala.mapper.MenuMapper;
import com.helloscala.mapper.RoleMapper;
import com.helloscala.mapper.RoleMenuMapper;
import com.helloscala.mapper.UserMapper;
import com.helloscala.vo.user.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义权限验证接口扩展
 */
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {
    private final UserMapper userMapper;
    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final RoleMapper roleMapper;


    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        User user = userMapper.selectById(loginId.toString());
        Integer roleId = user.getRoleId();
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.lambda().select(RoleMenu::getMenuId).eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(roleMenuQueryWrapper);

        Set<Integer> menuIdSet = roleMenus.stream().map(rm -> rm.getMenuId()).collect(Collectors.toSet());
        if (menuIdSet.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.lambda().select(Menu::getPerm).in(Menu::getId, menuIdSet);
        List<Menu> menus = menuMapper.selectList(menuQueryWrapper);
        return menus.stream()
            .filter(Objects::nonNull)
            .map(m -> m.getPerm()).distinct().toList();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return roleMapper.selectByUserId(loginId); // 从数据库查询这个账号id拥有的角色列表
    }
}
