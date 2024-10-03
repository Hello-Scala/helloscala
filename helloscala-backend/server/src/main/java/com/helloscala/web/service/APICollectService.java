package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.CollectService;
import com.helloscala.service.web.request.ListArticleRequest;
import com.helloscala.web.controller.article.view.APIRecommendedArticleView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class APICollectService {
    private final CollectService collectService;
    private final APIArticleService apiArticleService;


    public Page<APIRecommendedArticleView> listByPage(String userId) {
        Page<?> page = PageUtil.getPage();
        return apiArticleService.searchRecommendedArticlePage(userId, page, new ListArticleRequest());
    }

    public void collect(String userId, String articleId) {
        collectService.collect(userId, articleId);
    }

    public void cancel(String userId, String articleId) {
        collectService.cancel(userId, articleId);
    }
}
