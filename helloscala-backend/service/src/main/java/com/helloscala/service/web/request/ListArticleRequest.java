package com.helloscala.service.web.request;

import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListArticleRequest {
    private String userId;

    private Integer readType;

    private String title;

    private String tagId;

    public String categoryId;

    public Integer isPublish;

    public List<SortingRule> sortingRules;
}
