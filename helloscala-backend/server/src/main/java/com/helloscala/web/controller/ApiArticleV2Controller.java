package com.helloscala.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.AccessLimit;
import com.helloscala.common.annotation.BusinessLogger;
import com.helloscala.common.dto.article.ArticlePostDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.utils.DateHelper;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import com.helloscala.common.vo.article.ArticleInfoVO;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import com.helloscala.web.response.ListPublishedArticleResponse;
import com.helloscala.web.response.PublishedArticleView;
import com.helloscala.web.service.ApiArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/article")
@Tag(name = "Article management-V2")
@RequiredArgsConstructor
public class ApiArticleV2Controller {
    private final ApiArticleService articleService;

    @BusinessLogger(value = "Home page list articles", type = "search", desc = "list articles")
    @GetMapping(value = "/")
    @Operation(summary = "list articles", method = "GET")
    @ApiResponse(responseCode = "200", description = "article list")
    public Response<Page<RecommendedArticleVO>> selectArticleList(@RequestParam(name = "categoryId", required = false) String categoryId,
                                                                  @RequestParam(name = "tagId", required = false) String tagId,
                                                                  @RequestParam(name = "orderByDescColumn", required = false) String orderByDescColumn) {
        Page<RecommendedArticleVO> listArticleVOPage = articleService.selectArticleList(categoryId, tagId, orderByDescColumn);
        return ResponseHelper.ok(listArticleVOPage);
    }

    @BusinessLogger(value = "Get article detail", type = "search", desc = "get article detail")
    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get article detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Article detail")
    public Response<ArticleInfoVO> selectArticleInfo(@PathVariable(value = "id") String id) {
        ArticleInfoVO articleInfoVO = articleService.selectArticleInfo(id);
        return ResponseHelper.ok(articleInfoVO);
    }

    @GetMapping(value = "/search")
    @Operation(summary = "Search articles", method = "GET")
    @ApiResponse(responseCode = "200", description = "Search articles")
    public Response<Page<ApiArticleSearchVO>> searchArticle(@RequestParam(name = "keyword", required = false) String keyword) {
        Page<ApiArticleSearchVO> apiArticleSearchVOPage = articleService.searchArticle(keyword);
        return ResponseHelper.ok(apiArticleSearchVOPage);
    }

    @BusinessLogger(value = "Archive article", type = "search", desc = "Archive article")
    @GetMapping(value = "/archive")
    @Operation(summary = "Archive article", method = "GET")
    @ApiResponse(responseCode = "200", description = "Archive article")
    public Response<ListPublishedArticleResponse> archiveV2() {
        List<Article> articles = articleService.listPublished();

        Map<String, List<Article>> monthlyArticleMap = articles.stream().collect(Collectors.groupingBy(a -> DateHelper.toYearAndMonth(a.getCreateTime())));
        long total = monthlyArticleMap.values().stream().mapToLong(Collection::size).sum();
        List<ListPublishedArticleResponse.MonthlyArticleView> monthlyArticleViews = monthlyArticleMap.entrySet().stream().map(entry -> {
            List<PublishedArticleView> publishedArticleViews = entry.getValue().stream().map(article -> {
                PublishedArticleView publishedArticleView = new PublishedArticleView();
                publishedArticleView.setId(article.getId());
                publishedArticleView.setUserId(article.getUserId());
                publishedArticleView.setCategoryId(article.getCategoryId());
                publishedArticleView.setTitle(article.getTitle());
                publishedArticleView.setAvatar(article.getAvatar());
                publishedArticleView.setSummary(article.getSummary());
                publishedArticleView.setReadType(article.getReadType());
                publishedArticleView.setIsStick(article.getIsStick());
                publishedArticleView.setIsOriginal(article.getIsOriginal());
                publishedArticleView.setOriginalUrl(article.getOriginalUrl());
                publishedArticleView.setKeywords(article.getKeywords());
                publishedArticleView.setAddress(article.getAddress());
                publishedArticleView.setQuantity(article.getQuantity());
                publishedArticleView.setIsCarousel(article.getIsCarousel());
                publishedArticleView.setIsRecommend(article.getIsRecommend());
                publishedArticleView.setCreateTime(article.getCreateTime());
                publishedArticleView.setCreateMonthlyDate(article.getCreateTime());
                publishedArticleView.setUpdateTime(article.getUpdateTime());
                return publishedArticleView;
            }).toList();
            ListPublishedArticleResponse.MonthlyArticleView monthlyArticleView = new ListPublishedArticleResponse.MonthlyArticleView();
            monthlyArticleView.setMonth(entry.getKey());
            monthlyArticleView.setArticles(publishedArticleViews);
            return monthlyArticleView;
        }).toList();

        ListPublishedArticleResponse response = new ListPublishedArticleResponse();
        response.setTotal(total);
        response.setMonthlyArticles(monthlyArticleViews);
        return ResponseHelper.ok(response);
    }


    @AccessLimit
    @BusinessLogger(value = "Like article", type = "serach", desc = "Like article")
    @GetMapping(value = "/like")
    @Operation(summary = "Like article", method = "GET")
    @ApiResponse(responseCode = "200", description = "Like article")
    public EmptyResponse articleLike(@RequestParam(name = "articleId", required = true) String articleId) {
        articleService.articleLike(articleId);
        return ResponseHelper.ok();
    }

    @BusinessLogger(value = "Wechat official account check code", type = "search", desc = "Wechat official account check code")
    @GetMapping(value = "/checkCode")
    @Operation(summary = "Wechat official account check code", method = "GET")
    @ApiResponse(responseCode = "200", description = "Wechat official account check code")
    public EmptyResponse checkCode(@RequestParam(name = "code", required = true) String code) {
        articleService.checkCode(code);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @PostMapping(value = "/")
    @BusinessLogger(value = "Create article", type = "add", desc = "Create article")
    @Operation(summary = "Create article", method = "POST")
    @ApiResponse(responseCode = "200", description = "Create article")
    public EmptyResponse insertArticle(@RequestBody ArticlePostDTO dto) {
        articleService.insertArticle(dto);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @PutMapping(value = "/")
    @BusinessLogger(value = "Update article", type = "update", desc = "Update article")
    @Operation(summary = "Update article", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Update article")
    public EmptyResponse updateMyArticle(@RequestBody ArticlePostDTO dto) {
        articleService.updateMyArticle(dto);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @GetMapping(value = "/listByUserId")
    @BusinessLogger(value = "List article by user id", type = "search", desc = "List article by user id")
    @Operation(summary = "List article by user id", method = "GET")
    @ApiResponse(responseCode = "200", description = "List article by user id")
    public Response<Page<RecommendedArticleVO>> listByUserId(@RequestParam(name = "userId", required = true) String userId,
                                                             @RequestParam(name = "type", required = false) Integer type) {
        Page<RecommendedArticleVO> listArticleVOPage = articleService.listByUserId(userId, type);
        return ResponseHelper.ok(listArticleVOPage);
    }

    @SaCheckLogin
    @DeleteMapping(value = "/")
    @BusinessLogger(value = "Delete article", type = "delete", desc = "Delete article")
    @Operation(summary = "Delete article", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "Delete article")
    public EmptyResponse deleteMyArticle(@RequestParam(name = "id", required = true) String id) {
        articleService.deleteMyArticle(id);
        return ResponseHelper.ok();
    }

    @SaCheckLogin
    @GetMapping(value = "/selectMyArticleInfo")
    @BusinessLogger(value = "Get article detail", type = "search", desc = "Get article detail")
    @Operation(summary = "Get article detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get article detail")
    public Response<ArticlePostDTO> selectMyArticleInfo(@RequestParam(name = "id", required = true) String id) {
        ArticlePostDTO articlePostDTO = articleService.getById(id);
        return ResponseHelper.ok(articlePostDTO);
    }
}
