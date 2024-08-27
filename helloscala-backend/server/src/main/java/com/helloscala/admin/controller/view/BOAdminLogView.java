package com.helloscala.admin.controller.view;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class BOAdminLogView {
    @Schema(name = "主键ID")
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
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;
}
