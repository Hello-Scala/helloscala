package com.helloscala.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.vo.article.ApiArticleSearchVO;

import java.util.List;

public interface SearchStrategy {
    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List<  ApiArticleSearchVO  >} 文章列表
     */
    Page<ApiArticleSearchVO> searchArticle(String keywords);
}
