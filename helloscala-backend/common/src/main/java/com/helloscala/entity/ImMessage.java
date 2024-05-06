package com.helloscala.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.utils.DateUtil;
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
@TableName("b_im_message")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="ImMessage对象", description="群聊实体类")
public class ImMessage implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(name = "发送用户Id")
    private String toUserId;

    @Schema(name = "接收用户id")
    private String fromUserId;

    @Schema(name = "发送用户头像")
    private String toUserAvatar;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "ip")
    private String ip;

    @Schema(name = "ip地址")
    private String ipSource;

    @Schema(name = "是否撤回")
    private int isWithdraw;

    @Schema(name = "评论标记 1回复评论 2发表评论")
    private Integer commentMark;

    @Schema(name = "消息类型 1私聊 2群聊")
    private Integer code;

    @Schema(name = "消息是否已读 0未读 2已读")
    private Integer isRead;

    @Schema(name = "消息内容类型 1普通消息 2图片")
    private Integer type;

    @Schema(name = "通知类型 0系统通知 1：评论 2：关注 3点赞 4收藏 5私信")
    private Integer noticeType;

    @Schema(name = "文章id")
    private Integer articleId;

    @Schema(name = "@用户id 多个逗号分隔")
    private String atUserId;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;





}
