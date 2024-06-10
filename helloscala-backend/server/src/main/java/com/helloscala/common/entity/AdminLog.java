package com.helloscala.common.entity;

import cn.hutool.core.util.StrUtil;
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
@Schema(name="AdminLog对象", description="")
@TableName("b_admin_log")
public class AdminLog implements Serializable {
    @Schema(name = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "操作用户")
    private String username;

    @Schema(name = "请求接口")
    private String requestUrl;

    @Schema(name = "请求方式")
    private String type;

    @Schema(name = "操作名称")
    private String operationName;

    @Schema(name = "ip")
    private String ip;

    @Schema(name = "ip来源")
    private String source;

    @Schema(name = "请求参数")
    private String paramsJson;

    @Schema(name = "类地址")
    private String classPath;

    @Schema(name = "方法名")
    private String methodName;

    @Schema(name = "请求接口耗时")
    private Long spendTime;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    public AdminLog(){}

    public AdminLog(String ip, String source, String type, String requestUrl, String nickname,
                    String paramsJson, String classPath,
                    String methodName, String operationName,
                 Long spendTime) {
        this.ip = ip;
        this.source = StrUtil.isBlank(source) ? "未知": source;
        this.type = type;
        this.requestUrl = requestUrl;
        this.username = nickname;
        this.paramsJson = paramsJson;
        this.classPath = classPath;
        this.methodName = methodName;
        this.operationName = operationName;
        this.spendTime = spendTime;
    }
}
