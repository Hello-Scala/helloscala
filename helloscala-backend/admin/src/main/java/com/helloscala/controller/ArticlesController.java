package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.article.ArticleDTO;
import com.helloscala.entity.Article;
import com.helloscala.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/article")
@RequiredArgsConstructor
@Tag(name = "后台文章管理")
public class ArticlesController {

    private final ArticleService articleService;


    @GetMapping(value = "/list")
    @Operation(summary = "文章列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "文章列表")
    public ResponseResult selectArticlePage(@RequestParam(name = "title", required = false) String title,
                                            @RequestParam(name = "tagId", required = false) Integer tagId,
                                            @RequestParam(name = "categoryId", required = false) Integer categoryId,
                                            @RequestParam(name = "isPublish", required = false) Integer isPublish) {
        return articleService.selectArticlePage(title, tagId, categoryId, isPublish);
    }

    @GetMapping(value = "/info/{id}")
    @Operation(summary = "文章详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "文章详情")
    public ResponseResult selectArticleById(@PathVariable(value = "id") Long id) {
        return articleService.selectArticleById(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:article:add")
    @OperationLogger(value = "保存文章")
    @Operation(summary = "保存文章", method = "POST")
    @ApiResponse(responseCode = "200", description = "保存文章")
    public ResponseResult addArticle(@RequestBody ArticleDTO article) {
        return articleService.addArticle(article);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:article:update")
    @OperationLogger(value = "修改文章")
    @Operation(summary = "修改文章", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改文章")
    public ResponseResult updateArticle(@RequestBody ArticleDTO article) {
        return articleService.updateArticle(article);
    }


    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:article:delete")
    @OperationLogger(value = "删除文章")
    @Operation(summary = "删除文章", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除文章")
    public ResponseResult deleteBatchArticle(@RequestBody List<Long> ids) {
        return articleService.deleteBatchArticle(ids);
    }

    @PutMapping(value = "/top")
    @SaCheckPermission("system:article:top")
    @OperationLogger(value = "置顶文章")
    @Operation(summary = "置顶文章", method = "PUT")
    @ApiResponse(responseCode = "200", description = "置顶文章")
    public ResponseResult topArticle(@RequestBody ArticleDTO article) {
        return articleService.topArticle(article);
    }

    @PutMapping(value = "/pubOrShelf")
    @SaCheckPermission("system:article:pubOrShelf")
    @OperationLogger(value = "发布或下架文章")
    @Operation(summary = "发布或下架文章", method = "PUT")
    @ApiResponse(responseCode = "200", description = "发布或下架文章")
    public ResponseResult psArticle(@RequestBody Article article) {
        return articleService.psArticle(article);
    }

    @PostMapping(value = "/seo")
    @SaCheckPermission("system:article:seo")
    @OperationLogger(value = "批量文章SEO")
    @Operation(summary = "批量文章SEO", method = "POST")
    @ApiResponse(responseCode = "200", description = "文章SEO")
    public ResponseResult seoArticle(@RequestBody List<Long> ids) {
        return articleService.seoArticle(ids);
    }

    @GetMapping(value = "/reptile")
    @SaCheckPermission("system:article:reptile")
    @OperationLogger(value = "文章爬虫")
    @Operation(summary = "文章爬虫", method = "GET")
    @ApiResponse(responseCode = "200", description = "文章爬虫")
    public ResponseResult reptile(@RequestParam(name = "url", required = true) String url) {
        return articleService.reptile(url);
    }

    @GetMapping(value = "/randomImg")
    @Operation(summary = "随机获取一张图片", method = "GET")
    @ApiResponse(responseCode = "200", description = "随机获取一张图片")
    public ResponseResult randomImg() {
        return articleService.randomImg();
    }

}
