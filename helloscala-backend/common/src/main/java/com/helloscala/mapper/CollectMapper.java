package com.helloscala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.entity.Collect;
import com.helloscala.vo.article.ApiArticleListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectMapper extends BaseMapper<Collect> {

    /**
     * 我的收藏列表
     * @return
     */
    Page<ApiArticleListVO> selectCollectList(Page<ApiArticleListVO> tPage, @Param("userId") String userId);
}
