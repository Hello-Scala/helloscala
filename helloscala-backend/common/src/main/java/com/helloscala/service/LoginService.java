package com.helloscala.service;


import com.helloscala.common.ResponseResult;
import com.helloscala.dto.user.LoginDTO;

public interface LoginService {

    /**
     * 登录
     * @param vo
     * @return
     */
    ResponseResult login(LoginDTO vo);

    /**
     * 生成随机验证码
     * @return
     */
    ResponseResult getCaptcha();
}
