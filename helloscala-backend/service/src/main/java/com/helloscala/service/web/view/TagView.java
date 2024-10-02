package com.helloscala.service.web.view;

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
public class TagView {
    private String id;

    private String name;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "点击量")
    private Integer clickVolume;

    @Schema(name = "创建时间")
    private Date createTime;

    @Schema(name = "最后更新时间")
    private Date updateTime;
}
