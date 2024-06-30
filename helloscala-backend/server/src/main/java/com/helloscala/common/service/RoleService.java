package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.dto.role.RoleMenuDTO;
import com.helloscala.common.entity.Role;

import java.util.List;


public interface RoleService extends IService<Role> {

    Page<Role> selectRolePage(String name);

     void addRole(Role role);

    void updateRole(Role role);

    void deleteRole(List<Integer> ids);

    List<Integer> getCurrentUserRole();

    List<Integer> selectRoleMenuById(Integer roleId);

    void assignRoleMenus(RoleMenuDTO roleMenuDTO);
}
