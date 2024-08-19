package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleMapper extends BaseMapper<Role> {

    String queryByUserId(String userId);

    List<String> getRoleMenuIdList(String roleId);

    void insertBatchByRole(@Param("menus") List<String> menus, @Param("roleId") String roleId);

    List<String> selectAllMenuId();

}
