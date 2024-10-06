package com.helloscala.web.controller.home;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class APITagView {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "标签名称")
    private String name;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "点击量")
    private Integer clickVolume;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "最后更新时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;
}
