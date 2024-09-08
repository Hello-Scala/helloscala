package com.helloscala.service.service.article;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.web.view.ArticleSummaryView;

public interface ArticleSearchService {
    Page<ArticleSummaryView> executeSearchStrategy(Page<?> page, String searchMode, String keywords);
}
