package com.helloscala.vo.say;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiSayCommentVO {
    @Schema(name = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(name = "用户Id")
    private String userId;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "回复用户Id")
    private String replyUserId;

    @Schema(name = "回复用户昵称")
    private String replyUserNickname;

    @Schema(name = "说说id")
    private String sayId;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "ip")
    private String ip;

    @Schema(name = "ip来源")
    private String ipAddress;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "创建时间")
    private String createTimeStr;


}
