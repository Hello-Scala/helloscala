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
@EqualsAndHashCode(callSuper = false)
@Schema(name="UserLog对象", description="日志表")
@TableName("b_user_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLog implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "ip地址")
    private String ip;

    @Schema(name = "操作地址")
    private String address;

    @Schema(name = "操作类型")
    private String type;

    @Schema(name = "操作日志")
    private String description;

    @Schema(name = "操作模块")
    private String model;

    @Schema(name = "操作系统")
    private String accessOs;
    @Schema(name = "客户端类型")
    private String clientType;

    @Schema(name = "浏览器")
    private String browser;

    @Schema(name = "操作时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "操作结果")
    private String result;


}
