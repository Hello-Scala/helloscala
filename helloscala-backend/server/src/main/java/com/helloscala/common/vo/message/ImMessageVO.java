package com.helloscala.common.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ImMessageVO {

    @Schema(name = "主键id")
    private String id;

    @Schema(name = "消息类型")
    private Integer code;

    @Schema(name = "接收用户Id")
    private String toUserId;

    @Schema(name = "发送用户id")
    private String fromUserId;

    @Schema(name = "发送用户头像")
    private String fromUserAvatar;

    @Schema(name = "发送用户昵称")
    private String fromUserNickname;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "ip")
    private String ip;

    @Schema(name = "ip地址")
    private String ipSource;

    @Schema(name = "是否撤回")
    private int isWithdraw;

    @Schema(name = "文章id")
    private Integer articleId;

    @Schema(name = "文章标题")
    private String articleTitle;

    @Schema(name = "消息类型 1普通消息 2图片")
    private int type;

    @Schema(name = "是否已读 0:未读 1：已读")
    private int isRead;

    @Schema(name = "选中下标")
    private Integer index;

    @Schema(name = "评论标记 1回复评论 2发表评论")
    private Integer commentMark;

    @Schema(name = "通知类型 0系统通知 1：评论 2：关注 3点赞 4收藏 5私信")
    private Integer noticeType;

    @Schema(name = "@用户id 多个逗号分隔")
    private String atUserId;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "创建时间")
    private String createTimeStr;

    @Schema(name = "扩展信息")
    private Map<String, Object> ext;
}
