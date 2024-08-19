package com.helloscala.common.dto.role;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuDTO {

    private String roleId;

    private List<String> menuIds;
}
