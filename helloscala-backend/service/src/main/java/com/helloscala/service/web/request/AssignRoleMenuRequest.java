package com.helloscala.service.web.request;

import lombok.Data;

import java.util.List;

@Data
public class AssignRoleMenuRequest {
    private String roleId;

    private List<String> menuIds;

    private String requestBy;
}
