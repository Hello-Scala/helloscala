package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "FriendLink对象", description = "友情链接表")
@TableName("b_friend_link")
public class FriendLink implements Serializable {
    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "网站名称")
    private String name;

    @Schema(name = "网站地址")
    private String url;

    @Schema(name = "网站头像地址")
    private String avatar;

    @Schema(name = "网站描述")
    private String info;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "状态( 0,下架;1,申请;2:上架)")
    private Integer status;

    @Schema(name = "下架原因")
    private String reason;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;


}
