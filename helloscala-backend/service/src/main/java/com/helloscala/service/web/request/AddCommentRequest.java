package com.helloscala.service.web.request;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddCommentRequest {
    @Schema(name = "评论用户Id")
    private String userId;

    @Schema(name = "回复用户id")
    private String replyUserId;

    @Schema(name = "文章id")
    private String articleId;

    @Schema(name = "评论内容")
    private String content;

    @Schema(name = "父级id")
    private String parentId;

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

    private String requestBy;
}
