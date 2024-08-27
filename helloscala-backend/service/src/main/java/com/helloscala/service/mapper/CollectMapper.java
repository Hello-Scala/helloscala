package com.helloscala.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.entity.Collect;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.service.web.view.CollectCountView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface CollectMapper extends BaseMapper<Collect> {
    Page<RecommendedArticleVO> selectCollectList(Page<RecommendedArticleVO> tPage, @Param("userId") String userId);

    List<CollectCountView> countByArticles(@Param("articleIds") Set<String> articleIds);
}
