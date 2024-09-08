package com.helloscala.service.service.article;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.web.view.ArticleSummaryView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleSearchServiceImpl implements ArticleSearchService {

    private final Map<String, com.helloscala.service.service.file.strategy.ArticleSearchStrategy> searchStrategyMap;

    public Page<ArticleSummaryView> executeSearchStrategy(Page<?> page, String searchMode, String keywords) {
        return searchStrategyMap.get(searchMode).searchArticle(page, keywords);
    }

}
