package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("b_say_comment")
@Schema(name="SayComment", description="")
public class SayComment implements Serializable {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "用户Id")
    private String userId;

    @Schema(name = "回复用户Id")
    private String replyUserId;

    @Schema(name = "说说id")
    private String sayId;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "ip")
    private String ip;

    @Schema(name = "ip来源")
    private String ipAddress;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
