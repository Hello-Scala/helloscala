package com.helloscala.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.common.entity.Role;
import com.helloscala.common.entity.UserRole;
import com.helloscala.common.mapper.RoleMapper;
import com.helloscala.common.mapper.UserRoleMapper;
import com.helloscala.common.service.UserRoleService;
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
public class UserRoleServiceImpl implements UserRoleService {
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public List<String> listRoleCodes(String userId) {
        if (StrUtil.isBlank(userId)) {
            return List.of();
        }

        LambdaQueryWrapper<UserRole> userRoleQuery = new LambdaQueryWrapper<>();
        userRoleQuery.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQuery);

        Set<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        if (ObjectUtils.isEmpty(roleIds)) {
            return List.of();
        }

        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        return roles.stream().map(Role::getCode).toList();
    }
}
