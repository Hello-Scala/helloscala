package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.entity.Menu;
import com.helloscala.common.vo.menu.MenuOptionVO;
import com.helloscala.common.vo.menu.RouterVO;

import java.util.List;

public interface RoleMenuService {
    List<Menu> listAllMenus();

    List<Menu> listByRoleId(String roleId);

    List<String> listAllPerms();

    List<String> listRolePerms(String roleId);
}