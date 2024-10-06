package com.helloscala.common.config.satoken;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.helloscala.common.cache.RedisService;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.service.service.RedisConstants;
import com.helloscala.service.service.UserService;
import com.helloscala.service.web.request.UpdateLoginRequest;
import com.helloscala.service.web.view.UserView;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MySaTokenListener implements SaTokenListener {

    private static final Logger logger = LoggerFactory.getLogger(MySaTokenListener.class);
    private final UserService userService;
    private final RedisService redisService;

    @Value("${sa-token.timeout}")
    private Integer timeout;

    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        String id = String.valueOf(loginId);
        String ip = IpUtil.getIp();
        String cityInfo = IpUtil.getCityInfo(ip);
        UserAgent userAgent = IpUtil.getUserAgent(Objects.requireNonNull(IpUtil.getRequest()));
        String os = userAgent.getOperatingSystem().getName();
        String browser = userAgent.getBrowser().getName();

        UpdateLoginRequest updateLoginRequest = new UpdateLoginRequest();
        updateLoginRequest.setId(id);
        updateLoginRequest.setIp(ip);
        updateLoginRequest.setCity(cityInfo);
        updateLoginRequest.setOs(os);
        updateLoginRequest.setBrowser(browser);
        updateLoginRequest.setRequestBy(id);
        userService.updateLogin(updateLoginRequest);

        UserView userView = userService.get(id);

        String token = StpUtil.getTokenValueByLoginId(loginId);

        OnlineUser build = OnlineUser.builder()
                .avatar(userView.getAvatar())
                .ip(ip)
                .city(cityInfo)
                .loginTime(DateUtil.getNowDate())
                .os(os)
                .userId(id)
                .tokenValue(token)
                .nickname(userView.getNickname())
                .browser(browser).build();
        redisService.setCacheObject(RedisConstants.LOGIN_TOKEN + token, JSONUtil.toJsonStr(build), timeout, TimeUnit.SECONDS);
        logger.info("User login, useId:{}, token:{}", loginId, token);
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + tokenValue);
        logger.info("user logout, useId:{}, token:{}", loginId, tokenValue);
    }

    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + tokenValue);
        logger.info("User kicked out, useId:{}, token:{}", loginId, tokenValue);
    }

    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + tokenValue);
        logger.info("User replaced, useId:{}, token:{}", loginId, tokenValue);
    }

    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        String token = StpUtil.getTokenValueByLoginId(loginId);
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + token);
        logger.info("User disabled, useId:{}, token:{}", loginId, token);
    }

    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
        String token = StpUtil.getTokenValueByLoginId(loginId);
        redisService.deleteObject(RedisConstants.LOGIN_TOKEN + token);
        logger.info("User disabled, useId:{}, token:{}", loginId, token);
    }

    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {

    }

    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {

    }

    @Override
    public void doCreateSession(String id) {
        // ...
    }

    @Override
    public void doLogoutSession(String id) {
        // ...
        logger.info("user doLogoutSession,id:{}", id);
    }

    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

    }

}