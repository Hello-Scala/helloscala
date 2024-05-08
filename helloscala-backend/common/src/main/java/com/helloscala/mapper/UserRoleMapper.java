package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.entity.Menu;
import com.helloscala.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<Menu> selectMenuByUserId(Long id);
}
