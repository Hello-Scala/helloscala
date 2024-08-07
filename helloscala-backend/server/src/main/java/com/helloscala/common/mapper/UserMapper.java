package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.User;
import com.helloscala.common.vo.user.SystemUserInfoVO;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.vo.user.UserInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper extends BaseMapper<User> {

    List<Integer> getMenuId(String userId);

    Page<SystemUserInfoVO> selectPageRecord(@Param("page") Page<SystemUserInfoVO> page, @Param("username") String username, @Param("loginType") Integer loginType);

    SystemUserVO getById(String id);

    User selectNameAndPassword(@Param("username") String username, @Param("password") String password);

    void updateLoginInfo(@Param("loginId") String loginId, @Param("ip") String ip, @Param("cityInfo") String cityInfo,
                         @Param("os") String os, @Param("browser") String browser);

    UserInfoVO selectByUserName(String username);

    UserInfoVO selectInfoByUserId(String userId);
}
