package com.helloscala.service.web.view;

import lombok.Data;

@Data
public class ArticleTagCountView {
    private String tagId;

    private Integer count;
}
