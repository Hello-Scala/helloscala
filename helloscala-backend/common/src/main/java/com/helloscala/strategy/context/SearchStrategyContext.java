package com.helloscala.strategy.context;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.strategy.SearchStrategy;
import com.helloscala.vo.article.ApiArticleSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchStrategyContext {

    private final Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List<  ApiArticleSearchVO  >} 搜索文章
     */
    public Page<ApiArticleSearchVO> executeSearchStrategy(String searchMode, String keywords) {
        return searchStrategyMap.get(searchMode).searchArticle(keywords);
    }

}
