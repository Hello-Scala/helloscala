package com.helloscala.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserVO implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(name = "账号")
    private String username;

    @Schema(name = "昵称")
    private String nickname;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "个人简介")
    private String intro;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "角色Id")
    private Integer roleId;

    @Schema(name = "权限集合")
    private List<String> perms;
}
