package com.helloscala.utils;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;

public class CustomHttpUtil {

    private final static String ak = "f94be500c45148bc185be24a38c04ad3";

    private final static String sk = "27563ca627d5db0d57e831ca4de0f75f";

    /**
     * 发送api接口请求
     * @param url 接口地址
     * @return
     */
    public static String sendCuApiHttpUrl(String url) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("access-key", ak);
        paramMap.put("secret-key", sk);
        String result= HttpUtil.get(url, paramMap);
        return result;
    }
}
