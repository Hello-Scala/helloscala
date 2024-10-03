package com.helloscala.web.service;

import com.helloscala.service.service.ArticleService;
import com.helloscala.service.web.view.CategoryArticleCountView;
import com.helloscala.web.controller.category.APICategoryView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class APICategoryService {
    private final ArticleService articleService;

    public List<APICategoryView> list() {
        List<CategoryArticleCountView> categoryArticleCountViews = articleService.countAllCategories();
        return categoryArticleCountViews.stream().map(count -> {
            APICategoryView apiCategoryView = new APICategoryView();
            apiCategoryView.setId(count.getId());
            apiCategoryView.setName(count.getName());
            apiCategoryView.setIcon(count.getIcon());
            apiCategoryView.setArticleNum(count.getCount());
            return apiCategoryView;
        }).toList();
    }
}
