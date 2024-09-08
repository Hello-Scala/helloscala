package com.helloscala.admin.controller.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class BOExceptionLogView {
    @Schema(name = "主键")
    private String id;

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
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;
}
