package com.helloscala.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="DictData对象", description="字典数据表")
@TableName("b_dict_data")
public class DictData implements Serializable {
    @Schema(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "字典类型id")
    private Long dictId;

    @Schema(name = "字典标签")
    private String label;

    @Schema(name = "字典键值")
    private String value;

    @Schema(name = "回显样式")
    private String style;

    @Schema(name = "是否默认（1是 0否）")
    private String isDefault;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "是否发布(1:正常，0:停用)")
    private Integer status;

    @Schema(name = "备注")
    private String remark;

    @TableField(exist = false)
    private Dict dict;
}
