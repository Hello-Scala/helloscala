package com.helloscala.common.service;

import java.util.List;

/**
 * @author Steve Zou
 */
public interface UserRoleService {
    List<String> listRoleCodes(String userId);
}
