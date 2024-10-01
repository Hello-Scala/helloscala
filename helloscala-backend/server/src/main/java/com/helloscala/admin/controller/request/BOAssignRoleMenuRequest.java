package com.helloscala.admin.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class BOAssignRoleMenuRequest {
    private String roleId;

    private List<String> menuIds;
}
