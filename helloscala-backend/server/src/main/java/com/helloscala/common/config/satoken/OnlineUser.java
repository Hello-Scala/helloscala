package com.helloscala.common.config.satoken;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OnlineUser {
    private String loginId;
    private String userId;
    private String nickname;
    private String avatar;
    private String ip;
    private String os;
    private String city;
    private String browser;
    private String tokenValue;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date loginTime;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date lastActivityTime;
}

