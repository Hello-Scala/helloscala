package com.helloscala.service.web.request;

import lombok.Data;

@Data
public class SearchArticleRequest {
    private String title;
    private String tagId;
    private String categoryId;
    private Integer published;
}
