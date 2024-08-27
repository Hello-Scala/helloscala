package com.helloscala.service.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.vo.article.RecommendedArticleVO;


public interface ApiCollectService {
    Page<RecommendedArticleVO> selectCollectList(String userId);

    void collect(String userId, String articleId);

    void cancel(String userId, String articleId);
}
