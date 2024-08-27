package com.helloscala.service.service;


import com.helloscala.common.dto.user.LoginDTO;
import com.helloscala.common.vo.user.VerifyCodeVO;

public interface LoginService {
    String login(LoginDTO vo);

    VerifyCodeVO getCaptcha();
}
