package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
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

    public void updateUser(UserInfoDTO vo);

    public UserInfoVO selectUserInfoByToken(String token);

    public void authLogin(AuthResponse response, String source, HttpServletResponse httpServletResponse) throws IOException;

    public void sendEmailCode(String email);

    public void emailRegister(EmailRegisterDTO emailRegisterDTO);

    public void forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO);

    public ResponseResult getUserCount(String id);

    UserCountView getUserCounts(String userId);
}
