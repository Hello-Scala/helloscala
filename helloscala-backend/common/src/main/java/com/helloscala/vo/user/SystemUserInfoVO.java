package com.helloscala.vo.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


@Data
public class SystemUserInfoVO {

    private String id;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;

    @Schema(name = "最后登录时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date lastLoginTime;

    @Schema(name = "角色ID")
    private Integer roleId;

    @Schema(name = "IP地址")
    private String ipAddress;

    @Schema(name = "IP来源")
    private String ipSource;

    private Integer loginType;

    /**
     * 昵称
     * */
    private String nickname;

    /**
     * 头像
     * */
    private String avatar;
}
