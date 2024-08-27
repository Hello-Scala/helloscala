package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.dto.role.RoleMenuDTO;
import com.helloscala.service.entity.Role;
import com.helloscala.service.entity.RoleMenu;
import com.helloscala.service.mapper.RoleMapper;
import com.helloscala.service.mapper.RoleMenuMapper;
import com.helloscala.service.service.RoleService;
import com.helloscala.common.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public Page<Role> selectRolePage(String name) {
        Page<Role> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name), Role::getName, name);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(Role role) {
        baseMapper.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Role role) {
        baseMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
        LambdaQueryWrapper<RoleMenu> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.in(RoleMenu::getRoleId, ids);
        roleMenuMapper.delete(roleMenuQueryWrapper);
    }

    @Override
    public List<String> getCurrentUserRole() {
        String roleId = baseMapper.queryByUserId(StpUtil.getLoginIdAsString());
        return baseMapper.getRoleMenuIdList(roleId);
    }

    @Override
    public List<String> selectRoleMenuById(String roleId) {
        Role role = baseMapper.selectById(roleId);
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return baseMapper.selectAllMenuId();
        }
        return baseMapper.getRoleMenuIdList(roleId);
    }

    @Override
    public void assignRoleMenus(RoleMenuDTO roleMenuDTO) {
        Role role = baseMapper.selectById(roleMenuDTO.getRoleId());
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return;
        }

        LambdaQueryWrapper<RoleMenu> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuMapper.delete(roleMenuQueryWrapper);

        if (!roleMenuDTO.getMenuIds().isEmpty()) {
            baseMapper.insertBatchByRole(roleMenuDTO.getMenuIds(), roleMenuDTO.getRoleId());
        }
    }
}
