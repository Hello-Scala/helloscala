package com.helloscala.service.web.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class CollectView {
    @Schema(name = "主键id")
    private String id;

    @Schema(name = "评论用户Id")
    private String userId;

    @Schema(name = "文章id")
    private String articleId;

    @Schema(name = "创建时间")
    private Date createTime;
}
