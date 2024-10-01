package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Role;
import com.helloscala.service.web.request.CreateRoleRequest;
import com.helloscala.service.web.request.UpdateRoleRequest;
import com.helloscala.service.web.view.RoleView;

import java.util.List;


public interface RoleService extends IService<Role> {

    Page<RoleView> selectRolePage(Page<?> page, String name);

    void createRole(CreateRoleRequest request);

    void updateRole(UpdateRoleRequest request);

    void deleteRole(List<String> ids);

    List<String> getUserRoleMenuIds(String userId);

    List<String> listRoleMenuById(String roleId);
}
