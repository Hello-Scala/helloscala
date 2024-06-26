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
public class ImRoomListVO {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "接收用户id")
    private String receiveId;

    @Schema(name = "接收用户昵称")
    private String nickname;

    @Schema(name = "接收用户头像")
    private String avatar;

    @Schema(name = "未读消息条数")
    private int readNum;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(name = "创建时间")
    private String createTimeStr;
}
