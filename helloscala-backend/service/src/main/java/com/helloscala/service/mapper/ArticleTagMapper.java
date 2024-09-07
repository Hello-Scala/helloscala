package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.service.entity.ArticleTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    void insertIgnoreArticleTags(@Param("articleId") String articleId, @Param("tagIds") Set<String> tagIds);
}