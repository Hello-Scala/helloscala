package com.helloscala.entity;

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

    private static final long serialVersionUID=1L;


    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(name = "评论用户Id")
    private String userId;

    /**
     * 回复用户id
     */
    @Schema(name = "回复用户id")
    private String replyUserId;

    /**
     * 评论文章id
     */
    @Schema(name = "文章id")
    private Integer articleId;

    /**
     * 评论内容
     */
    @Schema(name = "评论内容")
    private String content;

    /**
     * 父评论id
     */
    @Schema(name = "父级id")
    private Integer parentId;

    /**
     * 创建时间
     */
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
