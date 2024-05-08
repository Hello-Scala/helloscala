package com.helloscala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.article.ArticleDTO;
import com.helloscala.entity.Article;

import java.util.List;


public interface ArticleService extends IService<Article> {

    /**
     * 后台分页获取文章
     * @return
     */
    ResponseResult selectArticlePage(String title,Integer tagId,Integer categoryId,Integer isPublish);

    /**
     * 后台根据主键获取文章详情
     * @param id 主键id
     * @return
     */
    ResponseResult selectArticleById(Long id);

    /**
     * 添加文章
     * @param article 文章对象
     * @return
     */
    ResponseResult addArticle(ArticleDTO article);

    /**
     * 修改文章
     * @param article 文章对象
     * @return
     */
    ResponseResult updateArticle(ArticleDTO article);


    /**
     * 后台批量删除文章
     * @param ids 文章id集合
     * @return
     */
    ResponseResult deleteBatchArticle(List<Long> ids);

    /**
     * 置顶文章
     * @param article 文章对象
     * @return
     */
    ResponseResult topArticle(ArticleDTO article);

    /**
     * 发布或下架文章
     * @param article 文章对象
     * @return
     */
    ResponseResult psArticle(Article article);

    /**
     * 百度seo
     * @param ids 文章id集合
     * @return
     */
    ResponseResult seoArticle(List<Long> ids);

    /**
     * 爬取文章
     * @param url 文章地址
     * @return
     */
    ResponseResult reptile(String url);

    /**
     * 随机获取图片
     * @return
     */
    ResponseResult randomImg();

}
