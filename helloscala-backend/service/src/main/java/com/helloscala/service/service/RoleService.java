package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.dto.role.RoleMenuDTO;
import com.helloscala.service.entity.Role;

import java.util.List;


public interface RoleService extends IService<Role> {

    Page<Role> selectRolePage(String name);

     void addRole(Role role);

    void updateRole(Role role);

    void deleteRole(List<String> ids);

    List<String> getCurrentUserRole();

    List<String> selectRoleMenuById(String roleId);

    void assignRoleMenus(RoleMenuDTO roleMenuDTO);
}
