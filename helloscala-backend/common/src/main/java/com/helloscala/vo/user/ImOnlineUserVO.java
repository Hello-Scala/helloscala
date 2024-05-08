package com.helloscala.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 聊天室在线用户列表
 */
@Data
public class ImOnlineUserVO {
    @Schema(name = "用户id")
    private String id;
    @Schema(name = "用户昵称")
    private String nickname;
    @Schema(name = "用户头像")
    private String avatar;
}
