package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.service.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> getMenuByUserId(Object loginId);

    List<String> selectButtonPermissions(@Param("userId") String userId, @Param("isAdmin") Boolean isAdmin);
}
