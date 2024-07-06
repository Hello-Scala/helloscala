package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.vo.article.RecommendedArticleVO;


public interface ApiCollectService {
    Page<RecommendedArticleVO> selectCollectList();

    void collect(Integer articleId);

    void cancel(Integer articleId);
}
