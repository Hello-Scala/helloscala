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
@TableName("b_feed_back")
@Schema(name = "FeedBack对象", description = "")
public class Feedback implements Serializable {
    @Schema(name = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(name = "用户id")
    private String userId;

    @Schema(name = "标题")
    private String title;

    @Schema(name = "详细内容")
    private String content;

    @Schema(name = "添加时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "图片地址")
    private String imgUrl;

    @Schema(name = "反馈类型 1:需求 2：缺陷")
    private Integer type;

    @Schema(name = "状态 0:未解决 1：解决")
    private Integer status;
}
