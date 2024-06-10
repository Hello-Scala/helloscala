package com.helloscala.common.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helloscala.common.utils.DateUtil;
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
public class SystemCommentVO {
    @Schema(name = "主键ID")
    private Integer id;

    @Schema(name = "用户头像")
    private String avatar;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "回复人昵称")
    private String replyNickname;

    @Schema(name = "文章标题")
    private String articleTitle;

    @Schema(name = "评论内容")
    private String content;

    @Schema(name = "评论时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}
