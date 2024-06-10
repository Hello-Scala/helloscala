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
@EqualsAndHashCode(callSuper = false)
@Schema(name="BExceptionLog对象", description="")
@TableName("b_exception_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionLog implements Serializable {
    @Schema(name = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "ip")
    private String ip;

    @Schema(name = "ip来源")
    private String ipSource;

    @Schema(name = "请求方法")
    private String method;

    @Schema(name = "描述")
    private String operation;

    @Schema(name = "参数")
    private String params;

    @Schema(name = "异常对象json格式")
    private String exceptionJson;

    @Schema(name = "异常简单信息,等同于e.getMessage")
    private String exceptionMessage;

    @Schema(name = "发生时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
