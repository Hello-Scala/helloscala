package com.helloscala.common.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVO {
    private String id;

    private String email;

    private Integer loginType;

    private String nickname;

    private String avatar;

    private String intro;

    private String webSite;

    private String address;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private String registerTime;

    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private String lastLoginTime;

    private int articleCount;

    private int collectCount;

    private int noteCount;

    private int commentCount;

    private int fansCount;

    private int watchCount;

    private int qiDayFollowedCount;

    private String bjCover;

    private String token;
}
