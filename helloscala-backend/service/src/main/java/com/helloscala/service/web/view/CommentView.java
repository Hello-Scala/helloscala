package com.helloscala.service.web.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Steve Zou
 */
@Data
public class CommentView {
    @Schema(name = "主键ID")
    private String id;

    @Schema(name = "用户头像")
    private String avatar;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "回复人昵称")
    private String replyNickname;

    @Schema(name = "文章标题")
    private String articleId;

    @Schema(name = "文章标题")
    private String articleTitle;

    @Schema(name = "评论内容")
    private String content;

    @Schema(name = "评论时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
