package com.helloscala.common.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评论")
public class CommentDTO {
    @Schema(name = "replyUserId", description = "回复用户id", type = "Integer")
    private Integer replyUserId;

    @Schema(name = "articleId", description = "文章id", type = "Integer")
    private Integer articleId;

    @NotBlank(message = "评论内容不能为空")
    @Schema(name = "commentContent", description = "评论内容", required = true, type = "String")
    private String commentContent;

    @Schema(name = "parentId", description = "评论父id", type = "Integer")
    private Integer parentId;


    @Schema(name = "userId", description = "用户ID", type = "Long")
    private Long userId;
}
