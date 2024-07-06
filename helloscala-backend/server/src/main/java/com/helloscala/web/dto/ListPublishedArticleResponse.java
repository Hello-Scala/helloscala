package com.helloscala.web.dto;

import com.helloscala.common.entity.Article;
import lombok.Data;

import java.util.List;

/**
 * @author Steve Zou
 */
@Data
public class ListPublishedArticleResponse {
    private Long total;

    private List<MonthlyArticleView> monthlyArticles;

    @Data
    public static class MonthlyArticleView {
        private String month;

        private List<PublishedArticleView> articles;
    }
}
