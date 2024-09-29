package com.helloscala.admin.controller.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author stevezou
 */
@Data
public class BOUserLogView {
    @Schema(name = "主键ID")
    private String id;

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
    private Date createTime;

    @Schema(name = "操作结果")
    private String result;
}
