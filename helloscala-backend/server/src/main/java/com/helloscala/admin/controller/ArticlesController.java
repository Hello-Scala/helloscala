package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/article")
@RequiredArgsConstructor
@Tag(name = "Article")
public class ArticlesController {

    private final ArticleService articleService;


    @GetMapping(value = "/list")
    @Operation(summary = "List articles", method = "GET")
    @ApiResponse(responseCode = "200", description = "文章列表")
    public ResponseResult selectArticlePage(@RequestParam(name = "title", required = false) String title,
                                            @RequestParam(name = "tagId", required = false) Integer tagId,
                                            @RequestParam(name = "categoryId", required = false) Integer categoryId,
                                            @RequestParam(name = "isPublish", required = false) Integer isPublish) {
        return articleService.selectArticlePage(title, tagId, categoryId, isPublish);
    }

    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get article detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Article detail")
    public ResponseResult selectArticleById(@PathVariable(value = "id") Long id) {
        return articleService.selectArticleById(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:article:add")
    @OperationLogger(value = "Save article")
    @Operation(summary = "Save article", method = "POST")
    @ApiResponse(responseCode = "200", description = "Save article")
    public ResponseResult addArticle(@RequestBody ArticleDTO article) {
        return articleService.addArticle(article);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:article:update")
    @OperationLogger(value = "Edit article")
    @Operation(summary = "Edit article", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Edit article")
    public ResponseResult updateArticle(@RequestBody ArticleDTO article) {
        return articleService.updateArticle(article);
    }


    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:article:delete")
    @OperationLogger(value = "Delete article")
    @Operation(summary = "Delete article", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除文章")
    public ResponseResult deleteBatchArticle(@RequestBody List<Long> ids) {
        return articleService.deleteBatchArticle(ids);
    }

    @PutMapping(value = "/top")
    @SaCheckPermission("system:article:top")
    @OperationLogger(value = "Topping article")
    @Operation(summary = "Topping article", method = "PUT")
    @ApiResponse(responseCode = "200", description = "置顶文章")
    public ResponseResult topArticle(@RequestBody ArticleDTO article) {
        return articleService.topArticle(article);
    }

    @PutMapping(value = "/pubOrShelf")
    @SaCheckPermission("system:article:pubOrShelf")
    @OperationLogger(value = "publish")
    @Operation(summary = "publish or withdraw", method = "PUT")
    @ApiResponse(responseCode = "200", description = "publish or withdraw")
    public ResponseResult psArticle(@RequestBody Article article) {
        return articleService.psArticle(article);
    }

    @PostMapping(value = "/seo")
    @SaCheckPermission("system:article:seo")
    @OperationLogger(value = "Bulk SEO")
    @Operation(summary = "Bulk SEO", method = "POST")
    @ApiResponse(responseCode = "200", description = "SEO")
    public ResponseResult seoArticle(@RequestBody List<Long> ids) {
        return articleService.seoArticle(ids);
    }

    // todo path
    @GetMapping(value = "/reptile")
    @SaCheckPermission("system:article:reptile")
    @OperationLogger(value = "fetch")
    @Operation(summary = "fetch", method = "GET")
    @ApiResponse(responseCode = "200", description = "fetch")
    public ResponseResult fetch(@RequestParam(name = "url", required = true) String url) {
        return articleService.retch(url);
    }

    @GetMapping(value = "/randomImg")
    @Operation(summary = "randomly get image", method = "GET")
    @ApiResponse(responseCode = "200", description = "randomly get image")
    public ResponseResult randomImg() {
        return articleService.randomImg();
    }

}
