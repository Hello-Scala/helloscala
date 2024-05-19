package com.helloscala.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesEncryptUtil {

    @Value("${sys.aesEncrypt.key:helloscala2024}")
    private String key;
    /**
     * 校验内容是否一致
     */
    public boolean validate(String target, String target1) {
        return target.equalsIgnoreCase(aesEncrypt(target1));
    }

    /**
     * AES加密
     *
     * @param password：密码
     * @return
     */
    public String aesEncrypt(String password){
        return SaSecureUtil.aesEncrypt(key, password);
    }
}
