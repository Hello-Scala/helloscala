package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.Constants;
import com.helloscala.common.ResultCode;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.service.ArticleService;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.vo.article.ArticleVO;
import com.helloscala.common.web.exception.ForbiddenException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<Page<ArticleVO>> selectArticlePage(@RequestParam(name = "title", required = false) String title,
                                                       @RequestParam(name = "tagId", required = false) String tagId,
                                                       @RequestParam(name = "categoryId", required = false) String categoryId,
                                                       @RequestParam(name = "isPublish", required = false) Integer isPublish) {
        Page<ArticleVO> articlePage = articleService.selectArticlePage(title, tagId, categoryId, isPublish);
        return ResponseHelper.ok(articlePage);
    }

    @GetMapping(value = "/info/{id}")
    @Operation(summary = "Get article detail", method = "GET")
    @ApiResponse(responseCode = "200", description = "Article detail")
    public Response<ArticleDTO> selectArticleById(@PathVariable(value = "id") String id) {
        ArticleDTO articleDTO = articleService.selectArticleById(id);
        return ResponseHelper.ok(articleDTO);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("system:article:add")
    @OperationLogger(value = "Save article")
    @Operation(summary = "Save article", method = "POST")
    @ApiResponse(responseCode = "200", description = "Save article")
    public EmptyResponse addArticle(@RequestBody ArticleDTO article) {
        article.setUserId(StpUtil.getLoginIdAsString());
        String ipAddress = IpUtil.getIp2region(IpUtil.getIp());
        articleService.addArticle(ipAddress, article);
        return ResponseHelper.ok();
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("system:article:update")
    @OperationLogger(value = "Edit article")
    @Operation(summary = "Edit article", method = "PUT")
    @ApiResponse(responseCode = "200", description = "Edit article")
    public EmptyResponse updateArticle(@RequestBody ArticleDTO article) {
        String userId = StpUtil.getLoginIdAsString();
        Article originArticle = articleService.getById(article.getId());
        if (ObjectUtil.isNull(originArticle)) {
            throw new NotFoundException(ResultCode.ARTICLE_NOT_FOUND.desc);
        }
        if (!originArticle.getUserId().equals(userId) && !StpUtil.hasRole(Constants.ADMIN_CODE)) {
            throw new ForbiddenException(ResultCode.NO_PERMISSION.desc);
        }
        articleService.updateArticle(userId, article);
        return ResponseHelper.ok();
    }


    @DeleteMapping(value = "/delete")
    @SaCheckPermission("system:article:delete")
    @OperationLogger(value = "Delete article")
    @Operation(summary = "Delete article", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除文章")
    public EmptyResponse deleteBatchArticle(@RequestBody List<String> ids) {
        articleService.deleteBatchArticle(ids);
        return ResponseHelper.ok();
    }

    @PutMapping(value = "/top")
    @SaCheckPermission("system:article:top")
    @OperationLogger(value = "Topping article")
    @Operation(summary = "Topping article", method = "PUT")
    @ApiResponse(responseCode = "200", description = "置顶文章")
    public EmptyResponse topArticle(@RequestBody ArticleDTO article) {
        articleService.stick(article.getId(), article.getIsStick() != 0);
        return ResponseHelper.ok();
    }

    // todo api redefine
    @PutMapping(value = "/pubOrShelf")
    @SaCheckPermission("system:article:pubOrShelf")
    @OperationLogger(value = "publish")
    @Operation(summary = "publish or withdraw", method = "PUT")
    @ApiResponse(responseCode = "200", description = "publish or withdraw")
    public EmptyResponse psArticle(@RequestBody Article article) {
        articleService.psArticle(article);
        return ResponseHelper.ok();
    }

    @PostMapping(value = "/seo")
    @SaCheckPermission("system:article:seo")
    @OperationLogger(value = "Bulk SEO")
    @Operation(summary = "Bulk SEO", method = "POST")
    @ApiResponse(responseCode = "200", description = "SEO")
    public EmptyResponse seoArticle(@RequestBody List<String> ids) {
        articleService.seoArticle(ids);
        return ResponseHelper.ok();
    }

    @GetMapping(value = "/randomImg")
    @Operation(summary = "randomly get image", method = "GET")
    @ApiResponse(responseCode = "200", description = "randomly get image")
    public Response<String> randomImg() {
        String s = articleService.randomImg();
        return ResponseHelper.ok(s);
    }

}
