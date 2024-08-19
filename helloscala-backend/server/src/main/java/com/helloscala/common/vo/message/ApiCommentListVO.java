package com.helloscala.common.vo.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCommentListVO {
    @Schema(name = "主键id")
    private String id;
    @Schema(name = "发表用户id")
    private String userId;
    @Schema(name = "评论用户id")
    private String replyUserId;

    @Schema(name = "评论内容")
    private String content;

    @Schema(name = "发表用户昵称")
    private String nickname;
    @Schema(name = "用户站点")
    private String webSite;

    @Schema(name = "回复用户昵称")
    private String replyNickname;

    @Schema(name = "回复用户站点")
    private String replyWebSite;

    @Schema(name = "发表用户头像")
    private String avatar;

    @Schema(name = "发表时间")
    private Date createTime;

    @Schema(name = "发表时间")
    private String createTimeStr;

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

    @Builder.Default
    @Schema(name = "子评论集合")
    List<ApiCommentListVO> children = new ArrayList<>();
}
