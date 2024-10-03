package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateArticleRequest;
import com.helloscala.admin.controller.request.BOUpdateArticleRequest;
import com.helloscala.admin.controller.view.BOArticleDetailView;
import com.helloscala.admin.controller.view.BOArticleView;
import com.helloscala.admin.service.helper.BOArticleHelper;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.ArticleService;
import com.helloscala.service.web.request.CreateArticleRequest;
import com.helloscala.service.web.request.ListArticleRequest;
import com.helloscala.service.web.request.UpdateArticleRequest;
import com.helloscala.service.web.view.ArticleDetailView;
import com.helloscala.service.web.view.ArticleView;
import com.helloscala.service.web.view.TagView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Steve Zou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOArticleService {
    private final ArticleService articleService;

    public Page<BOArticleView> selectArticlePage(String title, String tagId, String categoryId, Integer isPublish) {
        ListArticleRequest request = new ListArticleRequest();
        request.setTitle(title);
        request.setTagId(tagId);
        request.setCategoryId(categoryId);
        request.setIsPublish(isPublish);
        Page<?> page = PageUtil.getPage();
        Page<ArticleView> articlePage = articleService.listArticleSummary(page, request);

        return PageHelper.convertTo(articlePage, BOArticleHelper::buildBoArticleView);
    }

    public BOArticleDetailView getById(@PathVariable(value = "id") String id) {
        ArticleDetailView articleDetail = articleService.getDetailById(id);
        BOArticleDetailView boArticleDetailView = new BOArticleDetailView();
        boArticleDetailView.setId(articleDetail.getId());
        boArticleDetailView.setUserId(articleDetail.getUserId());
        boArticleDetailView.setTitle(articleDetail.getTitle());
        boArticleDetailView.setAvatar(articleDetail.getAvatar());
        boArticleDetailView.setReadType(articleDetail.getReadType());
        boArticleDetailView.setIsStick(articleDetail.getIsStick());
        boArticleDetailView.setIsOriginal(articleDetail.getIsOriginal());
        boArticleDetailView.setQuantity(articleDetail.getQuantity());
        boArticleDetailView.setCreateTime(articleDetail.getCreateTime());
        boArticleDetailView.setUpdateTime(articleDetail.getUpdateTime());
        boArticleDetailView.setIsPublish(articleDetail.getIsPublish());
        boArticleDetailView.setCategoryName(articleDetail.getCategoryName());
        boArticleDetailView.setSummary(articleDetail.getSummary());
        boArticleDetailView.setContent(articleDetail.getContent());
        boArticleDetailView.setContentMd(articleDetail.getContentMd());
        boArticleDetailView.setKeywords(articleDetail.getKeywords());
        boArticleDetailView.setOriginalUrl(articleDetail.getOriginalUrl());
        boArticleDetailView.setIsCarousel(articleDetail.getIsCarousel());
        boArticleDetailView.setIsRecommend(articleDetail.getIsRecommend());
        boArticleDetailView.setTags(articleDetail.getTags().stream().map(TagView::getName).toList());
        boArticleDetailView.setCreateTime(articleDetail.getCreateTime());
        boArticleDetailView.setUpdateTime(articleDetail.getUpdateTime());
        return boArticleDetailView;
    }

    public boolean exist(String articleId) {
        return articleService.existArticle(articleId);
    }

    public boolean ownArticles(String userId, List<String> articleIds) {
        if (ObjectUtils.isEmpty(articleIds)) {
            return true;
        }
        if (articleIds.size() == 1) {
            return articleService.ownArticle(userId, articleIds.get(0));
        } else {
            return articleService.ownArticles(userId, new HashSet<>(articleIds));
        }
    }

    public void create(String userId, String address, BOCreateArticleRequest request) {
        CreateArticleRequest createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setTitle(request.getTitle());
        createArticleRequest.setAvatar(request.getAvatar());
        createArticleRequest.setSummary(request.getSummary());
        createArticleRequest.setQuantity(request.getQuantity());
        createArticleRequest.setContent(request.getContent());
        createArticleRequest.setContentMd(request.getContentMd());
        createArticleRequest.setKeywords(request.getKeywords());
        createArticleRequest.setReadType(request.getReadType());
        createArticleRequest.setIsStick(request.getIsStick());
        createArticleRequest.setIsOriginal(request.getIsOriginal());
        createArticleRequest.setOriginalUrl(request.getOriginalUrl());
        createArticleRequest.setCategoryName(request.getCategoryName());
        createArticleRequest.setIsPublish(request.getIsPublish());
        createArticleRequest.setIsCarousel(request.getIsCarousel());
        createArticleRequest.setIsRecommend(request.getIsRecommend());
        createArticleRequest.setTags(request.getTags());
        articleService.addArticle(userId, address, createArticleRequest);
    }

    public void update(String userId, BOUpdateArticleRequest request) {
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();
        updateArticleRequest.setTitle(request.getTitle());
        updateArticleRequest.setAvatar(request.getAvatar());
        updateArticleRequest.setSummary(request.getSummary());
        updateArticleRequest.setQuantity(request.getQuantity());
        updateArticleRequest.setContent(request.getContent());
        updateArticleRequest.setContentMd(request.getContentMd());
        updateArticleRequest.setKeywords(request.getKeywords());
        updateArticleRequest.setReadType(request.getReadType());
        updateArticleRequest.setIsStick(request.getIsStick());
        updateArticleRequest.setIsOriginal(request.getIsOriginal());
        updateArticleRequest.setOriginalUrl(request.getOriginalUrl());
        updateArticleRequest.setCategoryName(request.getCategoryName());
        updateArticleRequest.setIsPublish(request.getIsPublish());
        updateArticleRequest.setIsCarousel(request.getIsCarousel());
        updateArticleRequest.setIsRecommend(request.getIsRecommend());
        updateArticleRequest.setTags(request.getTags());
        articleService.updateArticle(userId, request.getId(), updateArticleRequest);
    }

    public void deleteBatch(String userId, Set<String> ids) {
        articleService.deleteBatchArticle(ids);
        log.info("userId={}, deleted Articles ids=[{}]", userId, String.join(",", ids));
    }

    public void stick(String id, boolean stick) {
        articleService.stick(id, stick);
    }

    public void seoArticle(List<String> ids) {
        articleService.seoArticle(ids);
    }
}
