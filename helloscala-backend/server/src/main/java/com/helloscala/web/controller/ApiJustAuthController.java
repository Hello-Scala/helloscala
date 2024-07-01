package com.helloscala.web.controller;

import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.config.properties.GiteeConfigProperties;
import com.helloscala.common.config.properties.GithubConfigProperties;
import com.helloscala.common.config.properties.QqConfigProperties;
import com.helloscala.common.config.properties.WeiboConfigProperties;
import com.helloscala.common.dto.user.EmailForgetPasswordDTO;
import com.helloscala.common.dto.user.EmailLoginDTO;
import com.helloscala.common.dto.user.EmailRegisterDTO;
import com.helloscala.common.vo.user.UserInfoVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.dto.WechatAppletDTO;
import com.helloscala.web.service.ApiUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class ApiJustAuthController {
    private final GiteeConfigProperties giteeConfigProperties;
    private final QqConfigProperties qqConfigProperties;
    private final WeiboConfigProperties weiboConfigProperties;
    private final GithubConfigProperties githubConfigProperties;
    private final ApiUserService userService;


    @RequestMapping("/render/{source}")
    public Response<String> renderAuth(@PathVariable(value = "source") String source) {
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return ResponseHelper.ok(authorizeUrl);
    }

    // todo refactor
    @RequestMapping("/callback/{source}")
    public void login(AuthCallback callback, @PathVariable(value = "source") String source, HttpServletResponse httpServletResponse) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse response = authRequest.login(callback);
        userService.authLogin(response, source, httpServletResponse);
    }

    @AccessLimit
    @RequestMapping(value = "/emailLogin", method = RequestMethod.POST)
    @Operation(summary = "Login by email", method = "POST")
    @ApiResponse(responseCode = "200", description = "Login by email")
    public Response<UserInfoVO> emailLogin(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        UserInfoVO userInfoVO = userService.emailLogin(emailLoginDTO);
        return ResponseHelper.ok(userInfoVO);
    }

    @Operation(summary = "Is wechat login success", method = "GET")
    @ApiResponse(responseCode = "200", description = "Is wechat login success")
    @RequestMapping("/wechat/is_login")
    public Response<UserInfoVO> wxIsLogin(@RequestParam(name = "loginCode", required = true) String loginCode) {
        UserInfoVO userInfoVO = userService.wxIsLogin(loginCode);
        return ResponseHelper.ok(userInfoVO);
    }

    @Operation(summary = "Get verify code for wechat login", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get verify code for wechat login")
    @RequestMapping("/wechatLoginCode")
    public Response<String> getWechatLoginCode() {
        String wechatLoginCode = userService.getWechatLoginCode();
        return ResponseHelper.ok(wechatLoginCode);
    }

    @Operation(summary = "Send verify code to email", method = "GET")
    @ApiResponse(responseCode = "200", description = "Send verify code to email")
    @RequestMapping("/sendEmailCode")
    public EmptyResponse sendEmailCode(@RequestParam(name = "email", required = true) String email) {
        userService.sendEmailCode(email);
        return ResponseHelper.ok();
    }

    @AccessLimit
    @RequestMapping(value = "/emailRegister", method = RequestMethod.POST)
    @Operation(summary = "Register by email", method = "POST")
    @ApiResponse(responseCode = "200", description = "Register by email")
    public EmptyResponse emailRegister(@Valid @RequestBody EmailRegisterDTO emailRegisterDTO) {
        userService.emailRegister(emailRegisterDTO);
        return ResponseHelper.ok();
    }

    @AccessLimit
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.PUT)
    @Operation(summary = "Forget password", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Forget password")
    public EmptyResponse forgetPassword(@Valid @RequestBody EmailForgetPasswordDTO emailForgetPasswordDTO) {
        userService.forgetPassword(emailForgetPasswordDTO);
        return ResponseHelper.ok();
    }

    @RequestMapping(value = "/applet", method = RequestMethod.POST)
    @Operation(summary = "Login by applet", method = "GET")
    @ApiResponse(responseCode = "200", description = "Login by applet")
    public Response<UserInfoVO> appletLogin(@RequestBody WechatAppletDTO wechatAppletDTO) {
        UserInfoVO userInfoVO = userService.appletLogin(wechatAppletDTO);
        return ResponseHelper.ok(userInfoVO);
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
            throw new AuthException("Invalid authorization source!");
        }
        return authRequest;
    }

}
