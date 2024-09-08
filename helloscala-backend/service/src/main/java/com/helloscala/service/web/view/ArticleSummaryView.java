package com.helloscala.service.web.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ArticleSummaryView {
    @Schema(name = "id")
    private Long id;

    @Schema(name = "文章标题")
    private String title;

    @Schema(name = "文章内容")
    private String summary;
}
