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
@TableName("b_category")
@Schema(name="Category对象", description="博客分类表")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Schema(name = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "分类名称")
    private String name;

    @Schema(name = "点击量")
    private Integer clickVolume;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "图标")
    private String icon;

    @Schema(name = "创建时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;
}
