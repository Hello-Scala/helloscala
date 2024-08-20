package com.helloscala.common.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesEncryptUtil {

    @Value("${sys.aesEncrypt.key:helloscala2024}")
    private String key;

    public boolean validate(String target, String target1) {
        return target.equalsIgnoreCase(aesEncrypt(target1));
    }

    public String aesEncrypt(String password){
        return SaSecureUtil.aesEncrypt(key, password);
    }
}
