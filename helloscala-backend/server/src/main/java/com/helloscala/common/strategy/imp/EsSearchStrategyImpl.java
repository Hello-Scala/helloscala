package com.helloscala.common.strategy.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.entity.ArticleElastic;
import com.helloscala.common.enums.PublishEnum;
import com.helloscala.common.esmapper.EasyesMapper;
import com.helloscala.common.strategy.SearchStrategy;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import jakarta.annotation.Resource;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("EsSearchStrategyImpl")
public class EsSearchStrategyImpl implements SearchStrategy {
    private static final Logger logger = LoggerFactory.getLogger(EsSearchStrategyImpl.class);
    @Resource
    private EasyesMapper easyesMapper;

    @Override
    public Page<ApiArticleSearchVO> searchArticle(String keywords) {

        List<ApiArticleSearchVO> results = new ArrayList<>();

        LambdaEsQueryWrapper<ArticleElastic> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.multiMatchQuery(keywords, ArticleElastic::getTitle, ArticleElastic::getSummary)
            .eq(ArticleElastic::getIsPublish, PublishEnum.PUBLISH.getCode()).orderByDesc(ArticleElastic::getCreateTime);
        EsPageInfo<ArticleElastic> pageInfo = easyesMapper.pageQuery(wrapper, PageUtil.getPageNo().intValue(), PageUtil.getPageSize().intValue());

        pageInfo.getList().forEach(item -> results.add(BeanCopyUtil.copyObject(item, ApiArticleSearchVO.class)));
        logger.info("es search result: {}", results);

        Page<ApiArticleSearchVO> resultPage = new Page<>();
        resultPage.setRecords(results);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

}
