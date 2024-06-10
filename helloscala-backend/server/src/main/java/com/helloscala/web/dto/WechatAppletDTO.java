package com.helloscala.web.dto;

import lombok.Data;

@Data
public class WechatAppletDTO {
    private String code;

    private String avatarUrl;

    private String nickName;
}
