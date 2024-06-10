package com.helloscala.common.service;


import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.LoginDTO;

public interface LoginService {
    ResponseResult login(LoginDTO vo);

    ResponseResult getCaptcha();
}
