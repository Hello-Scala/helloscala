package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.role.RoleMenuDTO;
import com.helloscala.common.entity.Role;

import java.util.List;


public interface RoleService extends IService<Role> {

    ResponseResult selectRolePage(String name);

     ResponseResult addRole(Role role);

    ResponseResult updateRole(Role role);

    ResponseResult deleteRole(List<Integer> ids);

    ResponseResult getCurrentUserRole();

    ResponseResult selectRoleMenuById(Integer roleId);

    ResponseResult assignRoleMenus(RoleMenuDTO roleMenuDTO);
}
