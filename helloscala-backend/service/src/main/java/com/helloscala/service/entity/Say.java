package com.helloscala.service.entity;

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
@TableName("b_say")
@Schema(name="Say", description="")
public class Say implements Serializable {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(name = "用户Id")
    private String userId;

    @Schema(name = "图片地址 逗号分隔  最多九张")
    private String imgUrl;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "发表地址。可输入或者地图插件选择")
    private String address;

    @Schema(name = "是否开放查看  0未开放 1开放")
    private Integer isPublic;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
