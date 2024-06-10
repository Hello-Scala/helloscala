package com.helloscala.common.esmapper;


import com.helloscala.common.entity.ArticleElastic;
import org.dromara.easyes.core.kernel.BaseEsMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface EasyesMapper extends BaseEsMapper<ArticleElastic> {
}
