package com.helloscala.utils;

import cn.dev33.satoken.secure.SaSecureUtil;


public class AesEncryptUtil {

    // todo
    final static String key = "helloscala2024";
    /**
     * 校验内容是否一致
     */
    public static boolean validate(String target, String target1) {
        return target.equalsIgnoreCase(aesEncrypt(target1));
    }

    /**
     * AES加密
     *
     * @param password：密码
     * @return
     */
    public static String aesEncrypt(String password){
        return SaSecureUtil.aesEncrypt(key, password);
    }

    public static void main(String[] args) {
        System.out.println(aesEncrypt("123456"));
    }
}
