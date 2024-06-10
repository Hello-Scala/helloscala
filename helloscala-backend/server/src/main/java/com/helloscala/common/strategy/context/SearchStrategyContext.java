package com.helloscala.common.strategy.context;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.strategy.SearchStrategy;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchStrategyContext {

    private final Map<String, SearchStrategy> searchStrategyMap;

    public Page<ApiArticleSearchVO> executeSearchStrategy(String searchMode, String keywords) {
        return searchStrategyMap.get(searchMode).searchArticle(keywords);
    }

}
