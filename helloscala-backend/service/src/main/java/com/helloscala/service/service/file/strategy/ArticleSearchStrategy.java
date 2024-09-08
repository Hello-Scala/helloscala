package com.helloscala.service.service.file.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.web.view.ArticleSummaryView;

public interface ArticleSearchStrategy {
    Page<ArticleSummaryView> searchArticle(Page<?> page, String keywords);
}
