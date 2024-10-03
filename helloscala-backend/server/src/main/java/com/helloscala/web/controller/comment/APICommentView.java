package com.helloscala.web.controller.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class APICommentView {
    @Schema(name = "主键ID")
    private String id;

    @Schema(name = "发表用户id")
    private String userId;

    @Schema(name = "用户头像")
    private String avatar;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "用户站点")
    private String webSite;

    @Schema(name = "评论用户id")
    private String replyUserId;

    @Schema(name = "回复人昵称")
    private String replyNickname;

    @Schema(name = "回复用户站点")
    private String replyWebSite;

    @Schema(name = "文章标题")
    private String articleId;

    @Schema(name = "文章标题")
    private String articleTitle;

    @Schema(name = "评论内容")
    private String content;

    @Schema(name = "浏览器")
    private String browser;

    @Schema(name = "浏览器版本")
    private String browserVersion;

    @Schema(name = "电脑系统")
    private String system;

    @Schema(name = "系统版本")
    private String systemVersion;

    @Schema(name = "ip地址")
    private String ipAddress;

    @Schema(name = "评论时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @Deprecated
    @Schema(name = "发表时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING, timezone = "GMT+8")
    private Date createTimeStr;

    private List<APICommentView> children;
}
