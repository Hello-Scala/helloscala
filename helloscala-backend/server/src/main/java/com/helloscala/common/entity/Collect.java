package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("b_collect")
@Schema(name="Collect", description="")
public class Collect implements Serializable {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(name = "评论用户Id")
    private String userId;

    @Schema(name = "文章id")
    private Integer articleId;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
