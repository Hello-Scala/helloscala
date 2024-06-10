package com.helloscala.common.vo.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class SystemCategoryListVO {
    @Schema(name = "主键id")
    private Long id;

    @Schema(name = "分类名称")
    private String name;

    @Schema(name = "点击量")
    private Integer clickVolume;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "图标")
    private String icon;

    @Schema(name = "文章量")
    private int articleCount;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "最后更新时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;
}
