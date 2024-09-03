package com.helloscala.admin.controller.view;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class BODictView {
    @Schema(name = "主键")
    private String id;

    @Schema(name = "字典名称")
    private String name;

    @Schema(name = "字典类型")
    private String type;

    @Schema(name = "状态(1:正常，0:停用)")
    private Integer status;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "修改时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;

    @Schema(name = "排序")
    private Integer sort;
}
