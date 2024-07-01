package com.helloscala.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.vo.article.ListArticleVO;


public interface ApiCollectService {
    Page<ListArticleVO> selectCollectList();

    void collect(Integer articleId);

    void cancel(Integer articleId);
}
