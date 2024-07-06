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
@EqualsAndHashCode(callSuper = false)
@TableName("b_comment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="Comment对象", description="评论实体类")
public class Comment implements Serializable {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "评论用户Id")
    private String userId;

    @Schema(name = "回复用户id")
    private String replyUserId;

    @Schema(name = "文章id")
    private Long articleId;

    @Schema(name = "评论内容")
    private String content;

    @Schema(name = "父级id")
    private Integer parentId;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "浏览器名")
    private String browser;

    @Schema(name = "浏览器版本")
    private String browserVersion;

    @Schema(name = "系统名")
    @TableField("`system`")
    private String system;

    @Schema(name = "系统版本")
    private String systemVersion;
    @Schema(name = "ip地址")
    private String ipAddress;
}
