package com.helloscala.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.utils.DateUtil;
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
@TableName("b_emoji")
@Schema(name="Emoji", description="")
public class Emoji implements Serializable {
    private static final long serialVersionUID=1L;


    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(name = "用户Id")
    private String userId;


    @Schema(name = "表情url地址")
    private String url;

    @Schema(name = "是否置顶")
    private Integer isStick;


    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
