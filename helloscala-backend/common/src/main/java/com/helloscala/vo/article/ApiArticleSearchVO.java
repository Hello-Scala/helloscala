package com.helloscala.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiArticleSearchVO {

    @Schema(name = "id")
    private Long id;


    @Schema(name = "文章标题")
    private String title;


    @Schema(name = "文章内容")
    private String summary;
}
