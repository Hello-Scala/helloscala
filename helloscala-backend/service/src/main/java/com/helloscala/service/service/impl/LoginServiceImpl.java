package com.helloscala.service.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.helloscala.common.Constants;
import com.helloscala.common.dto.user.LoginDTO;
import com.helloscala.service.entity.User;
import com.helloscala.service.mapper.UserMapper;
import com.helloscala.service.service.LoginService;
import com.helloscala.service.service.RedisService;
import com.helloscala.common.utils.AesEncryptUtil;
import com.helloscala.common.vo.user.VerifyCodeVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.FailedDependencyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.helloscala.common.RedisConstants.CAPTCHA_CODE;
import static com.helloscala.common.ResultCode.ERROR_EXCEPTION_MOBILE_CODE;
import static com.helloscala.common.ResultCode.ERROR_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AesEncryptUtil aesEncryptUtil;

    private final UserMapper userMapper;

    private final RedisService redisService;


    @Override
    public VerifyCodeVO getCaptcha() {
        String captchaKey = UUID.randomUUID().toString();
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(140, 40, 4, 0);
        redisService.setCacheObject(CAPTCHA_CODE + captchaKey,shearCaptcha.getCode(),1,TimeUnit.MINUTES);
        String base64String = "";
        try {
            base64String = "data:image/png;base64," + shearCaptcha.getImageBase64();
        } catch (Exception e) {
            log.error("Failed to create captcha!", e);
            throw new FailedDependencyException("Failed to create captcha!", e);
        }
        return VerifyCodeVO.builder().captchaKey(captchaKey).captchaBase64(base64String).build();
    }

    @Override
    public String login(LoginDTO dto) {
        if (dto.getCaptchaKey() == null || dto.getCaptchaCode() == null) {
            throw new BadRequestException(ERROR_EXCEPTION_MOBILE_CODE.desc);
        }

        Object cachedCodeString = redisService.getCacheObject(CAPTCHA_CODE + dto.getCaptchaKey());

        if (cachedCodeString == null || !dto.getCaptchaCode().equalsIgnoreCase(cachedCodeString.toString())) {
            throw new BadRequestException(ERROR_EXCEPTION_MOBILE_CODE.desc);
        }

        User user = userMapper.selectNameAndPassword(dto.getUsername(), aesEncryptUtil.aesEncrypt(dto.getPassword()));
        if (user == null){
            throw new BadRequestException(ERROR_PASSWORD.desc);
        }

        if (dto.getRememberMe()) {
            StpUtil.login(user.getId(),new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        }else {
            StpUtil.login(user.getId(),"system");
        }
        StpUtil.getSession().set(Constants.CURRENT_USER,userMapper.getById(user.getId()));
        return StpUtil.getTokenValueByLoginId(user.getId());
    }


}
