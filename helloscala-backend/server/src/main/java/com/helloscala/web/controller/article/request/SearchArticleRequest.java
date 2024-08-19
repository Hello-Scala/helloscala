package com.helloscala.web.controller.article.request;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class SearchArticleRequest {
    private String categoryId;

    private String tagId;
}
