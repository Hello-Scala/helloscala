package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.common.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleMapper extends BaseMapper<Role> {

    Integer queryByUserId(Object userId);

    List<Integer> getRoleMenuIdList(Integer roleId);

    void insertBatchByRole(@Param("menus") List<Integer> menus, @Param("roleId") Integer roleId);

    List<String> selectByUserId(Object loginId);

    List<Integer> selectAllMenuId();

}
