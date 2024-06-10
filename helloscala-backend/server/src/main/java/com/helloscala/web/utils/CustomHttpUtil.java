package com.helloscala.web.utils;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;

public class CustomHttpUtil {
    private static final String ak = "f94be500c45148bc185be24a38c04ad3";
    private static final String sk = "27563ca627d5db0d57e831ca4de0f75f";

    public static String sendCuApiHttpUrl(String url) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("access-key", ak);
        paramMap.put("secret-key", sk);
        return HttpUtil.get(url, paramMap);
    }
}
