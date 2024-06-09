package com.helloscala.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.helloscala.common.Constants;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.user.LoginDTO;
import com.helloscala.entity.User;
import com.helloscala.exception.BusinessException;
import com.helloscala.mapper.UserMapper;
import com.helloscala.service.LoginService;
import com.helloscala.service.RedisService;
import com.helloscala.utils.AesEncryptUtil;
import com.helloscala.vo.user.VerifyCodeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.helloscala.common.RedisConstants.CAPTCHA_CODE;
import static com.helloscala.common.ResultCode.ERROR_EXCEPTION_MOBILE_CODE;
import static com.helloscala.common.ResultCode.ERROR_PASSWORD;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AesEncryptUtil aesEncryptUtil;

    private final UserMapper userMapper;

    private final RedisService redisService;


    @Override
    public ResponseResult getCaptcha() {
        String captchaKey = UUID.randomUUID().toString();
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(140, 40, 4, 0);
        // 存入redis并设置过期时间为1分钟
        redisService.setCacheObject(CAPTCHA_CODE + captchaKey,shearCaptcha.getCode(),1,TimeUnit.MINUTES);
        String base64String = "";
        try {
            base64String = "data:image/png;base64," + shearCaptcha.getImageBase64();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseResult.success(VerifyCodeVO.builder().captchaKey(captchaKey).captchaBase64(base64String).build());
    }

    @Override
    public ResponseResult login(LoginDTO dto) {

        // 校验验证码是否为空
        if (dto.getCaptchaKey() == null || dto.getCaptchaCode() == null) {
            throw new BusinessException(ERROR_EXCEPTION_MOBILE_CODE);
        }

        Object cachedCodeString = redisService.getCacheObject(CAPTCHA_CODE + dto.getCaptchaKey());

        // 确保转换为字符串后不为空
        if (cachedCodeString == null || !dto.getCaptchaCode().equalsIgnoreCase(cachedCodeString.toString())) {
            throw new BusinessException(ERROR_EXCEPTION_MOBILE_CODE);
        }

        //校验用户名和密码
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
