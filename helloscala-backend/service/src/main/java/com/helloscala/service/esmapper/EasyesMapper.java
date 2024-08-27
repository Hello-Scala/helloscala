package com.helloscala.service.esmapper;


import com.helloscala.service.entity.ArticleElastic;
import org.dromara.easyes.core.kernel.BaseEsMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface EasyesMapper extends BaseEsMapper<ArticleElastic> {
}
