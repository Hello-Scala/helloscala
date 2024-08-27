package com.helloscala.service.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_message")
@Schema(name = "Message对象", description = "")
public class Message implements Serializable {
    @Schema(name = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "昵称")
    private String nickname;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "ip地址")
    private String ipAddress;

    private Integer time;

    @Schema(name = "ip来源")
    private String ipSource;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;
}
