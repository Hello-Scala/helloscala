package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helloscala.service.entity.ArticleTag;
import com.helloscala.service.web.view.ArticleTagCountView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    void insertIgnoreArticleTags(@Param("articleId") String articleId, @Param("tagIds") Set<String> tagIds);

    List<ArticleTagCountView> countByTagIds(Set<String> tagIds);
}
