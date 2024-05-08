package com.helloscala.controller;

import com.helloscala.annotation.AccessLimit;
import com.helloscala.common.ResponseResult;
import com.helloscala.config.properties.GiteeConfigProperties;
import com.helloscala.config.properties.GithubConfigProperties;
import com.helloscala.config.properties.QqConfigProperties;
import com.helloscala.config.properties.WeiboConfigProperties;
import com.helloscala.dto.WechatAppletDTO;
import com.helloscala.dto.user.EmailForgetPasswordDTO;
import com.helloscala.dto.user.EmailLoginDTO;
import com.helloscala.dto.user.EmailRegisterDTO;
import com.helloscala.service.ApiUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeiboRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class ApiJustAuthController {


    private final GiteeConfigProperties giteeConfigProperties;

    private final QqConfigProperties qqConfigProperties;

    private final WeiboConfigProperties weiboConfigProperties;

    private final GithubConfigProperties githubConfigProperties;

    private  final ApiUserService userService;


    /**
     * 通过JustAuth的AuthRequest拿到第三方的授权链接，并跳转到该链接页面
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/render/{source}")
    public ResponseResult renderAuth(HttpServletResponse response, @PathVariable(value = "source") String source) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return ResponseResult.success(authorizeUrl);
    }

    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     * authRequest.login通过code向第三方请求用户数据
     *
     * @param callback 第三方回调时的入参
     * @return 第三方平台的用户信息
     */
    @RequestMapping("/callback/{source}")
    public void login(AuthCallback callback, @PathVariable(value = "source") String source,  HttpServletResponse httpServletResponse) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse response = authRequest.login(callback);
        userService.authLogin(response,source,httpServletResponse);
    }

    @AccessLimit
    @RequestMapping(value = "/emailLogin",method = RequestMethod.POST)
    @Operation(summary = "账号密码登录", method = "POST")
    @ApiResponse(responseCode = "200", description = "账号密码登录")
    public ResponseResult emailLogin(@Valid @RequestBody EmailLoginDTO emailLoginDTO){
        return userService.emailLogin(emailLoginDTO);
    }

    @Operation(summary = "判断用户是否微信登录成功", method = "GET")
    @ApiResponse(responseCode = "200", description = "判断用户是否微信登录成功")
    @RequestMapping("/wechat/is_login")
    public ResponseResult wxIsLogin(@RequestParam(name = "loginCode", required = true) String loginCode) {
        return userService.wxIsLogin(loginCode);
    }

    @Operation(summary = "获取微信登录验证码", method = "GET")
    @ApiResponse(responseCode = "200", description = "获取微信登录验证码")
    @RequestMapping("/wechatLoginCode")
    public ResponseResult getWechatLoginCode() {
        return userService.getWechatLoginCode();
    }

    @Operation(summary = "发送邮箱验证码", method = "GET")
    @ApiResponse(responseCode = "200", description = "发送邮箱验证码")
    @RequestMapping("/sendEmailCode")
    public ResponseResult sendEmailCode(@RequestParam(name = "email", required = true) String email) {
        return userService.sendEmailCode(email);
    }

    @AccessLimit
    @RequestMapping(value = "/emailRegister",method = RequestMethod.POST)
    @Operation(summary = "邮箱注册", method = "POST")
    @ApiResponse(responseCode = "200", description = "邮箱注册")
    public ResponseResult emailRegister(@Valid @RequestBody EmailRegisterDTO emailRegisterDTO){
        return userService.emailRegister(emailRegisterDTO);
    }

    @AccessLimit
    @RequestMapping(value = "/forgetPassword",method = RequestMethod.PUT)
    @Operation(summary = "忘记密码", method = "PUT")
    @ApiResponse(responseCode = "200", description = "忘记密码")
    public ResponseResult forgetPassword(@Valid @RequestBody EmailForgetPasswordDTO emailForgetPasswordDTO){
        return userService.forgetPassword(emailForgetPasswordDTO);
    }

    @RequestMapping(value = "/applet",method = RequestMethod.POST)
    @Operation(summary = "小程序登录", method = "GET")
    @ApiResponse(responseCode = "200", description = "小程序登录")
    public ResponseResult appletLogin(@RequestBody WechatAppletDTO wechatAppletDTO){
        return userService.appletLogin(wechatAppletDTO);
    }

    /**
     * 创建授权request
     *
     * @return AuthRequest
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteeConfigProperties.getAppId())
                        .clientSecret(giteeConfigProperties.getAppSecret())
                        .redirectUri(giteeConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId(qqConfigProperties.getAppId())
                        .clientSecret(qqConfigProperties.getAppSecret())
                        .redirectUri(qqConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId(weiboConfigProperties.getAppId())
                        .clientSecret(weiboConfigProperties.getAppSecret())
                        .redirectUri(weiboConfigProperties.getRedirectUrl())
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(githubConfigProperties.getAppId())
                        .clientSecret(githubConfigProperties.getAppSecret())
                        .redirectUri(githubConfigProperties.getRedirectUrl())
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("授权地址无效");
        }
        return authRequest;
    }

}
