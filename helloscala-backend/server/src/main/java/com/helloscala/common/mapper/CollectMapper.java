package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.Collect;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectMapper extends BaseMapper<Collect> {
    Page<RecommendedArticleVO> selectCollectList(Page<RecommendedArticleVO> tPage, @Param("userId") String userId);
}
