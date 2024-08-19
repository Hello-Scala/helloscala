package com.helloscala.common.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.entity.Role;
import com.helloscala.common.entity.RoleMenu;
import com.helloscala.common.entity.User;
import com.helloscala.common.mapper.MenuMapper;
import com.helloscala.common.mapper.RoleMapper;
import com.helloscala.common.mapper.RoleMenuMapper;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.service.RoleMenuService;
import com.helloscala.common.service.UserRoleService;
import com.helloscala.common.service.UserService;
import com.helloscala.common.vo.user.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {
    private final UserService userService;
    private final RoleMenuService roleMenuService;
    private final UserRoleService userRoleService;
    @Value("${sys.admin.roleId:1}")
    private String adminRoleId;

    @Value("${sys.admin.hasAllPermission:true}")
    private Boolean adminHasAllPermission;

    /**
     * admin have all menus' permission
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        User user = userService.getById(loginId.toString());
        String roleId = user.getRoleId();
        if (adminHasAllPermission && adminRoleId.equals(roleId)) {
            return roleMenuService.listAllPerms();
        } else {
            return roleMenuService.listRolePerms(roleId);
        }
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = userService.getById(loginId.toString());
        String roleId = user.getRoleId();
        return userRoleService.listRoleCodes(roleId);
    }
}
