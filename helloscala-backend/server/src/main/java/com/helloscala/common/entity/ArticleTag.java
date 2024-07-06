package com.helloscala.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_article_tag")
@Schema(name = "b_article_tag", description = "文章标签")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTag implements Serializable {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "文章id")
    private Long articleId;

    @Schema(name = "标签id")
    private Long tagId;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
