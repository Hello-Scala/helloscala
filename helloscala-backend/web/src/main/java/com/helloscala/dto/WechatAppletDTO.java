package com.helloscala.dto;

import lombok.Data;

@Data
public class WechatAppletDTO {

    /**
     *
     * @return 登录code
     */
    private String code;

    /**
     *
     * @return 用户头像
     */
    private String avatarUrl;
    /**
     *
     * @return 用户昵称
     */
    private String nickName;
}
