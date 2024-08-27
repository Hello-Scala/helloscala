package com.helloscala.service.web.view;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class CategoryArticleCountView {
    private String categoryId;

    private Long count;
}
