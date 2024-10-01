package com.helloscala.service.service;

import com.helloscala.service.entity.Menu;
import com.helloscala.service.web.request.AssignRoleMenuRequest;

import java.util.List;

public interface RoleMenuService {
    List<Menu> listAllMenus();

    List<Menu> listByRoleId(String roleId);

    List<String> listAllPerms();

    List<String> listRolePerms(String roleId);

    void assignRoleMenus(AssignRoleMenuRequest request);
}
