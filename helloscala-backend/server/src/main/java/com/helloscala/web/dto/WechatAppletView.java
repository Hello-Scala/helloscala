package com.helloscala.web.dto;

import lombok.Data;

@Data
public class WechatAppletView {
    private String code;

    private String avatarUrl;

    private String nickName;
}
