package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOAssignRoleMenuRequest;
import com.helloscala.admin.controller.request.BOCreateRoleRequest;
import com.helloscala.admin.controller.request.BOUpdateRoleRequest;
import com.helloscala.admin.controller.view.BORoleView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.RoleMenuService;
import com.helloscala.service.service.RoleService;
import com.helloscala.service.web.request.AssignRoleMenuRequest;
import com.helloscala.service.web.request.CreateRoleRequest;
import com.helloscala.service.web.request.UpdateRoleRequest;
import com.helloscala.service.web.view.RoleView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BORoleService {
    private RoleService roleService;
    private RoleMenuService roleMenuService;

    public Page<BORoleView> listByPage(String name) {
        Page<?> page = PageUtil.getPage();
        Page<RoleView> rolePage = roleService.selectRolePage(page, name);
        return PageHelper.convertTo(rolePage, role -> {
            BORoleView roleView = new BORoleView();
            roleView.setId(role.getId());
            roleView.setCode(role.getCode());
            roleView.setName(role.getName());
            roleView.setRemarks(role.getRemarks());
            roleView.setCreateTime(role.getCreateTime());
            roleView.setUpdateTime(role.getUpdateTime());
            roleView.setMenus(role.getMenus());
            return roleView;
        });
    }

    public List<String> getUserRoleMenuIds(String userId) {
        return roleService.getUserRoleMenuIds(userId);
    }

    public List<String> listRoleMenuById(String roleId) {
        return roleService.listRoleMenuById(roleId);
    }

    public void assignRoleMenus(String userId, BOAssignRoleMenuRequest request) {
        AssignRoleMenuRequest assignRoleMenuRequest = new AssignRoleMenuRequest();
        assignRoleMenuRequest.setRoleId(request.getRoleId());
        assignRoleMenuRequest.setMenuIds(request.getMenuIds());
        assignRoleMenuRequest.setRequestBy(userId);
        roleMenuService.assignRoleMenus(assignRoleMenuRequest);
    }

    public void create(String userId, BOCreateRoleRequest request) {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setCode(request.getCode());
        createRoleRequest.setName(request.getName());
        createRoleRequest.setRemarks(request.getRemarks());
        createRoleRequest.setRequestBy(userId);
        roleService.createRole(createRoleRequest);
    }

    public void update(String userId, BOUpdateRoleRequest request) {
        UpdateRoleRequest updateRoleRequest = new UpdateRoleRequest();
        updateRoleRequest.setId(request.getId());
        updateRoleRequest.setCode(request.getCode());
        updateRoleRequest.setName(request.getName());
        updateRoleRequest.setRemarks(request.getRemarks());
        updateRoleRequest.setRequestBy(userId);
        roleService.updateRole(updateRoleRequest);
    }

    public void delete(String userId, List<String> ids) {
        roleService.deleteRole(ids);
        log.info("userId={}, deleted Role ids=[{}]", userId, String.join(",", ids));
    }
}
