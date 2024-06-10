package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.user.LoginDTO;
import com.helloscala.common.entity.User;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.service.LoginService;
import com.helloscala.common.service.RedisService;
import com.helloscala.common.utils.AesEncryptUtil;
import com.helloscala.common.vo.user.VerifyCodeVO;
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
    public ResponseResult getCaptcha() {
        String captchaKey = UUID.randomUUID().toString();
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(140, 40, 4, 0);
        redisService.setCacheObject(CAPTCHA_CODE + captchaKey,shearCaptcha.getCode(),1,TimeUnit.MINUTES);
        String base64String = "";
        try {
            base64String = "data:image/png;base64," + shearCaptcha.getImageBase64();
        } catch (Exception e) {
            log.error("Failed to create captcha!", e);
        }

        return ResponseResult.success(VerifyCodeVO.builder().captchaKey(captchaKey).captchaBase64(base64String).build());
    }

    @Override
    public ResponseResult login(LoginDTO dto) {
        if (dto.getCaptchaKey() == null || dto.getCaptchaCode() == null) {
            throw new BusinessException(ERROR_EXCEPTION_MOBILE_CODE);
        }

        Object cachedCodeString = redisService.getCacheObject(CAPTCHA_CODE + dto.getCaptchaKey());

        if (cachedCodeString == null || !dto.getCaptchaCode().equalsIgnoreCase(cachedCodeString.toString())) {
            throw new BusinessException(ERROR_EXCEPTION_MOBILE_CODE);
        }

        User user = userMapper.selectNameAndPassword(dto.getUsername(), aesEncryptUtil.aesEncrypt(dto.getPassword()));
        if (user == null){
            throw new BusinessException(ERROR_PASSWORD.desc);
        }

        if (dto.getRememberMe()) {
            StpUtil.login(user.getId(),new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        }else {
            StpUtil.login(user.getId(),"system");
        }
        StpUtil.getSession().set(Constants.CURRENT_USER,userMapper.getById(user.getId()));
        return ResponseResult.success(StpUtil.getTokenValueByLoginId(user.getId()));
    }


}
