package com.helloscala.common.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.vo.article.ApiArticleSearchVO;

import java.util.List;

public interface SearchStrategy {
    Page<ApiArticleSearchVO> searchArticle(String keywords);
}
