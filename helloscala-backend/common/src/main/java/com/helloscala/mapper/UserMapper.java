package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.entity.User;
import com.helloscala.vo.user.SystemUserInfoVO;
import com.helloscala.vo.user.SystemUserVO;
import com.helloscala.vo.user.UserInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper extends BaseMapper<User> {

    List<Integer> getMenuId(String userId);

    Page<SystemUserInfoVO> selectPageRecord(@Param("page") Page<SystemUserInfoVO> page, @Param("username")String username, @Param("loginType")Integer loginType);

    SystemUserVO getById(Object id);

    User selectNameAndPassword(@Param("username") String username, @Param("password") String password);

    void updateLoginInfo(@Param("loginId")Object loginId,@Param("ip") String ip, @Param("cityInfo")String cityInfo,
                         @Param("os") String os,@Param("browser") String browser);

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    UserInfoVO selectByUserName(String username);

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    UserInfoVO selectInfoByUserId(Object userId);



}
