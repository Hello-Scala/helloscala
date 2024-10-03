package com.helloscala.web.controller.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class APIAddCommentRequest {
    @Schema(name = "回复用户id")
    private String replyUserId;

    @Schema(name = "文章id")
    private String articleId;

    @Schema(name = "评论内容")
    private String content;

    @Schema(name = "父级id")
    private String parentId;
}
