package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.EmailForgetPasswordDTO;
import com.helloscala.common.dto.user.EmailLoginDTO;
import com.helloscala.common.dto.user.EmailRegisterDTO;
import com.helloscala.common.dto.user.UserInfoDTO;
import com.helloscala.web.dto.WechatAppletDTO;
import jakarta.servlet.http.HttpServletResponse;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;

import java.io.IOException;

public interface ApiUserService {
    public ResponseResult emailLogin(EmailLoginDTO emailLoginDTO);

    public ResponseResult getWechatLoginCode();

    public String wechatLogin(WxMpXmlMessage message);

    public ResponseResult wxIsLogin(String loginCode);

    public ResponseResult selectUserInfo(String userId);

    public ResponseResult updateUser(UserInfoDTO vo);

    public ResponseResult selectUserInfoByToken(String token);

    public void authLogin(AuthResponse response, String source, HttpServletResponse httpServletResponse) throws IOException;

    public ResponseResult sendEmailCode(String email);

    public ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO);

    public ResponseResult forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO);

    public ResponseResult getUserCount(String id);

    ResponseResult appletLogin(WechatAppletDTO wechatAppletDTO);

}
