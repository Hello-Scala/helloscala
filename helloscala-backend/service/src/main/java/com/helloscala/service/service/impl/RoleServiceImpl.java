package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.utils.ListHelper;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Role;
import com.helloscala.service.entity.RoleMenu;
import com.helloscala.service.mapper.RoleMapper;
import com.helloscala.service.mapper.RoleMenuMapper;
import com.helloscala.service.service.RoleService;
import com.helloscala.service.web.request.CreateRoleRequest;
import com.helloscala.service.web.request.UpdateRoleRequest;
import com.helloscala.service.web.view.RoleView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public Page<RoleView> selectRolePage(Page<?> page, String name) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name), Role::getName, name);
        Page<Role> rolePage = baseMapper.selectPage(PageHelper.of(page), queryWrapper);
        Set<String> roleIds = rolePage.getRecords().stream().map(Role::getId).collect(Collectors.toSet());
        Map<String, List<String>> roleMenuIdMap = listRoleMenuIdMap(roleIds);
        return PageHelper.convertTo(rolePage, role -> {
            RoleView roleView = new RoleView();
            roleView.setId(role.getId());
            roleView.setCode(role.getCode());
            roleView.setName(role.getName());
            roleView.setRemarks(role.getRemarks());
            roleView.setCreateTime(role.getCreateTime());
            roleView.setUpdateTime(role.getUpdateTime());
            roleView.setMenus(ListHelper.ofNullable(roleMenuIdMap.get(role.getId())));
            return roleView;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(CreateRoleRequest request) {
        Role role = new Role();
        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setRemarks(request.getRemarks());
        role.setCreateTime(new Date());
        baseMapper.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(UpdateRoleRequest request) {
        Role role = baseMapper.selectById(request.getId());
        if (Objects.isNull(role)) {
            throw new NotFoundException("Role not found, id={}!", request.getId());
        }

        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setRemarks(request.getRemarks());
        role.setUpdateTime(new Date());
        baseMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(List<String> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return;
        }
        baseMapper.deleteBatchIds(ids);
        LambdaQueryWrapper<RoleMenu> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.in(RoleMenu::getRoleId, ids);
        roleMenuMapper.delete(roleMenuQueryWrapper);
    }

    @Override
    public List<String> getUserRoleMenuIds(String userId) {
        String roleId = baseMapper.queryByUserId(userId);
        Map<String, List<String>> roleMenuIdMap = listRoleMenuIdMap(Set.of(roleId));
        return roleMenuIdMap.values().stream().flatMap(Collection::stream).toList();
    }

    @Override
    public List<String> listRoleMenuById(String roleId) {
        Role role = baseMapper.selectById(roleId);
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return baseMapper.selectAllMenuId();
        }
        Map<String, List<String>> roleMenuIdMap = listRoleMenuIdMap(Set.of(roleId));
        return roleMenuIdMap.values().stream().flatMap(Collection::stream).toList();
    }

    private Map<String, List<String>> listRoleMenuIdMap(Set<String> roleIds) {
        if (ObjectUtil.isEmpty(roleIds)) {
            return Map.of();
        }
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleMenu::getRoleId, roleIds);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);
        Map<String, List<RoleMenu>> roleMenuMap = ListHelper.ofNullable(roleMenus).stream().collect(Collectors.groupingBy(RoleMenu::getRoleId));
        HashMap<String, List<String>> roleMenuIdMap = new HashMap<>();
        roleMenuMap.forEach((k, v) -> roleMenuIdMap.put(k, v.stream().map(RoleMenu::getMenuId).toList()));
        return roleMenuIdMap;
    }
}
