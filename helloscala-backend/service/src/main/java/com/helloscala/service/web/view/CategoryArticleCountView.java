package com.helloscala.service.web.view;

import lombok.Data;

/**
 * @author Steve Zou
 */
@Data
public class CategoryArticleCountView {
    private String id;

    private String name;

    private String icon;

    private Integer count;
}
