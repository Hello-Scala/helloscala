package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.helloscala.annotation.AccessLimit;
import com.helloscala.annotation.BusinessLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.article.ArticlePostDTO;
import com.helloscala.service.ApiArticleService;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/article")
@Tag(name = "文章API-V1")
@RequiredArgsConstructor
public class ApiArticleController {

    private final ApiArticleService articleService;

    @BusinessLogger(value = "首页-用户访问首页",type = "查询",desc = "查询所有文章")
    @GetMapping(value = "/")
    @Operation(summary = "文章列表", method = "GET")
    @ApiResponse(responseCode = "200", description = "文章列表")
    public ResponseResult selectArticleList(@RequestParam(name = "categoryId", required = false) Integer categoryId,
                                            @RequestParam(name = "tagId", required = false) Integer tagId,
                                            @RequestParam(name = "orderByDescColumn", required = false) String orderByDescColumn) {
        return  articleService.selectArticleList(categoryId,tagId,orderByDescColumn);
    }

    @BusinessLogger(value = "门户-用户查看文章详情",type = "查询",desc = "查看文章详情")
    @GetMapping(value = "/info/{id}")
    @Operation(summary = "文章详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "文章详情")
    public ResponseResult selectArticleInfo(@PathVariable(value = "id") Integer id) {
        return articleService.selectArticleInfo(id);
    }

    @GetMapping(value = "/search")
    @Operation(summary = "用户搜索文章", method = "GET")
    @ApiResponse(responseCode = "200", description = "用户搜索文章")
    public ResponseResult searchArticle(@RequestParam(name = "keyword", required = false) String keyword) {
        return articleService.searchArticle(keyword);
    }

    @BusinessLogger(value = "首页-归档",type = "查询",desc = "归档")
    @GetMapping(value = "/archive")
    @Operation(summary = "归档", method = "GET")
    @ApiResponse(responseCode = "200", description = "归档")
    public ResponseResult archive() {
        return  articleService.archive();
    }


    @AccessLimit
    @BusinessLogger(value = "门户-文章点赞",type = "查询",desc = "文章点赞")
    @GetMapping(value = "/like")
    @Operation(summary = "文章点赞", method = "GET")
    @ApiResponse(responseCode = "200", description = "文章点赞")
    public ResponseResult articleLike(@RequestParam(name = "articleId", required = true) Integer articleId) {
        return articleService.articleLike(articleId);
    }

    @BusinessLogger(value = "文章详情-校验公众号验证码",type = "查询",desc = "校验公众号验证码")
    @GetMapping(value = "/checkCode")
    @Operation(summary = "校验公众号验证码", method = "GET")
    @ApiResponse(responseCode = "200", description = "校验公众号验证码")
    public ResponseResult checkCode(@RequestParam(name = "code", required = true) String code) {
        return articleService.checkCode(code);
    }

    @SaCheckLogin
    @PostMapping(value = "/")
    @BusinessLogger(value = "添加文章",type = "添加",desc = "添加文章")
    @Operation(summary = "添加文章", method = "POST")
    @ApiResponse(responseCode = "200", description = "添加文章")
    public ResponseResult insertArticle(@RequestBody ArticlePostDTO dto) {
        return articleService.insertArticle(dto);
    }

    @SaCheckLogin
    @PutMapping(value = "/")
    @BusinessLogger(value = "修改我的文章",type = "修改",desc = "修改我的文章")
    @Operation(summary = "修改我的文章", method = "PUT")
    @ApiResponse(responseCode = "200", description = "修改我的文章")
    public ResponseResult updateMyArticle(@RequestBody ArticlePostDTO dto) {
        return articleService.updateMyArticle(dto);
    }

    @SaCheckLogin
    @GetMapping(value = "/selectArticleByUserId")
    @BusinessLogger(value = "根据用户id获取文章",type = "查询",desc = "根据用户id获取文章")
    @Operation(summary = "根据用户id获取文章", method = "GET")
    @ApiResponse(responseCode = "200", description = "根据用户id获取文章")
    public ResponseResult selectArticleByUserId(@RequestParam(name = "categoryId", required = true) String userId,
                                                @RequestParam(name = "type", required = false) Integer type) {
        return articleService.selectArticleByUserId(userId,type);
    }

    @SaCheckLogin
    @DeleteMapping(value = "/")
    @BusinessLogger(value = "删除我的文章",type = "删除",desc = "删除我的文章")
    @Operation(summary = "删除我的文章", method = "DELETE")
    @ApiResponse(responseCode = "200", description = "删除我的文章")
    public ResponseResult deleteMyArticle(@RequestParam(name = "id", required = true) Long id) {
        return articleService.deleteMyArticle(id);
    }

    @SaCheckLogin
    @GetMapping(value = "/selectMyArticleInfo")
    @BusinessLogger(value = "我的文章详情",type = "查询",desc = "我的文章详情")
    @Operation(summary = "我的文章详情", method = "GET")
    @ApiResponse(responseCode = "200", description = "我的文章详情")
    public ResponseResult selectMyArticleInfo(@RequestParam(name = "id", required = true) Long id) {
        return articleService.selectMyArticleInfo(id);
    }

    @PostMapping(value = "/readMarkdownFile")
    @Operation(summary = "md文件添加文章", method = "POST")
    @ApiResponse(responseCode = "200", description = "md文件添加文章")
    public ResponseResult readMdFile(@RequestPart(name = "file", required = true) MultipartFile file) {
        return articleService.readMarkdownFile(file);
    }

}
