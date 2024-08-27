package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Collect;
import com.helloscala.service.web.view.CollectCountView;

import java.util.List;
import java.util.Set;


public interface CollectService extends IService<Collect> {
    List<CollectCountView> countByArticles(Set<String> articleIds);

    List<String> listCollectArticleIds(String userId);
}
