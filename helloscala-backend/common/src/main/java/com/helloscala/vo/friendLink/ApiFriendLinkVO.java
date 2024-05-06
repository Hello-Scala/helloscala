package com.helloscala.vo.friendLink;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiFriendLinkVO {

    @Schema(name = "网站名称")
    private String name;

    @Schema(name = "网站地址")
    private String url;

    @Schema(name = "网站头像地址")
    private String avatar;

    @Schema(name = "网站描述")
    private String info;

}
