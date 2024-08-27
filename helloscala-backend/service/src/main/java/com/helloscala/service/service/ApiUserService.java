package com.helloscala.service.service;

import com.helloscala.common.dto.user.EmailForgetPasswordDTO;
import com.helloscala.common.dto.user.EmailLoginDTO;
import com.helloscala.common.dto.user.EmailRegisterDTO;
import com.helloscala.common.dto.user.UserInfoDTO;
import com.helloscala.common.vo.user.UserCountView;
import com.helloscala.common.vo.user.UserInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;

import java.io.IOException;

public interface ApiUserService {
    public UserInfoVO emailLogin(EmailLoginDTO emailLoginDTO);

    public String getWechatLoginCode();

    public String wechatLogin(WxMpXmlMessage message);

    public UserInfoVO wxIsLogin(String loginCode);

    public UserInfoVO selectUserInfo(String userId);

    void updateUser(UserInfoDTO vo);

    UserInfoVO selectUserInfoByToken(String token);

    void authLogin(AuthResponse response, String source, HttpServletResponse httpServletResponse) throws IOException;

    void sendEmailCode(String email);

    void emailRegister(EmailRegisterDTO emailRegisterDTO);

    void forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO);


    UserCountView getUserCounts(String userId);
}
