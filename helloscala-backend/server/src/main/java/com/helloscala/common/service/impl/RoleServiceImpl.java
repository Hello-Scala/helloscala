package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.role.RoleMenuDTO;
import com.helloscala.common.entity.Role;
import com.helloscala.common.mapper.RoleMapper;
import com.helloscala.common.service.RoleService;
import com.helloscala.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public ResponseResult selectRolePage(String name) {
        Page<Role> data = baseMapper.selectPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), new LambdaQueryWrapper<Role>()
                .like(StrUtil.isNotBlank(name), Role::getName, name));
        return ResponseResult.success(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addRole(Role role) {
        baseMapper.insert(role);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateRole(Role role) {
        baseMapper.updateById(role);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteRole(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        ids.forEach(id -> baseMapper.delByRoleId(id, null));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getCurrentUserRole() {
        Integer roleId = baseMapper.queryByUserId(StpUtil.getLoginIdAsString());
        List<Integer> list = baseMapper.getRoleMenuIdList(roleId);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult selectRoleMenuById(Integer roleId) {
        Role role = baseMapper.selectById(roleId);
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return ResponseResult.success(baseMapper.selectAllMenuId());
        }
        List<Integer> list = baseMapper.getRoleMenuIdList(roleId);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult assignRoleMenus(RoleMenuDTO roleMenuDTO) {
        Role role = baseMapper.selectById(roleMenuDTO.getRoleId());
        if (role.getCode().equals(Constants.ADMIN_CODE)) {
            return ResponseResult.success();
        }
        baseMapper.delByRoleId(roleMenuDTO.getRoleId(), null);
        if (roleMenuDTO.getMenuIds().size() > 0) {
            baseMapper.insertBatchByRole(roleMenuDTO.getMenuIds(), roleMenuDTO.getRoleId());
        }
        return ResponseResult.success();
    }
}
