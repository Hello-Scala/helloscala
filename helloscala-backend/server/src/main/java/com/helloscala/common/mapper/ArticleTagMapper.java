package com.helloscala.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.dto.article.ArticlePostDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.ArticleTag;
import com.helloscala.common.vo.article.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
}
