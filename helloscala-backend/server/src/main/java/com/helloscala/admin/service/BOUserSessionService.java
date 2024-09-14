package com.helloscala.admin.service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.helloscala.admin.controller.request.BOLoginRequest;
import com.helloscala.admin.controller.view.BOVerificationCodeView;
import com.helloscala.admin.service.helper.RedisConstants;
import com.helloscala.common.Constants;
import com.helloscala.common.utils.AesEncryptUtil;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.FailedDependencyException;
import com.helloscala.service.entity.User;
import com.helloscala.service.service.RedisService;
import com.helloscala.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author stevezou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOUserSessionService {
    private final AesEncryptUtil aesEncryptUtil;
    private final RedisService redisService;
    private final UserService userService;

    public BOVerificationCodeView getCaptcha() {
        String captchaKey = UUID.randomUUID().toString();
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(140, 40, 4, 0);
        redisService.setCacheObject(RedisConstants.CAPTCHA_CODE + captchaKey, shearCaptcha.getCode(), 1, TimeUnit.MINUTES);
        String base64String = "";
        try {
            base64String = "data:image/png;base64," + shearCaptcha.getImageBase64();
        } catch (Exception e) {
            log.error("Failed to create captcha!", e);
            throw new FailedDependencyException("Failed to create captcha!", e);
        }
        BOVerificationCodeView verificationCodeView = new BOVerificationCodeView();
        verificationCodeView.setCaptchaKey(captchaKey);
        verificationCodeView.setCaptchaBase64(base64String);
        return verificationCodeView;
    }

    public String login(BOLoginRequest request) {
        if (request.getCaptchaKey() == null || request.getCaptchaCode() == null) {
            throw new BadRequestException("Captcha key or captcha code missing!");
        }

        Object cachedCodeString = redisService.getCacheObject(RedisConstants.CAPTCHA_CODE + request.getCaptchaKey());

        if (cachedCodeString == null || !request.getCaptchaCode().equalsIgnoreCase(cachedCodeString.toString())) {
            throw new BadRequestException("Code expired or invalid!");
        }

        User user = userService.getByNameAndPwd(request.getUsername(), aesEncryptUtil.aesEncrypt(request.getPassword()));
        if (user == null) {
            throw new BadRequestException("Username or password incorrect!");
        }

        if (request.getRememberMe()) {
            StpUtil.login(user.getId(), new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        } else {
            StpUtil.login(user.getId(), "system");
        }

        StpUtil.getSession().set(Constants.CURRENT_USER, user);
        return StpUtil.getTokenValueByLoginId(user.getId());
    }
}
