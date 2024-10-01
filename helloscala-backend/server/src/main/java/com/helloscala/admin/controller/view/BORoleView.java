package com.helloscala.admin.controller.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BORoleView {
    @Schema(name = "主键ID")
    private String id;

    @Schema(name = "角色编码")
    private String code;

    @Schema(name = "角色名称")
    private String name;

    @Schema(name = "角色描述")
    private String remarks;

    @Schema(name = "创建时间")
    private Date createTime;

    @Schema(name = "最后更新时间")
    private Date updateTime;

    private List<String> menus;
}
