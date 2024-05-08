package com.helloscala.strategy.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.entity.ArticleElastic;
import com.helloscala.enums.PublishEnum;
import com.helloscala.esmapper.EasyesMapper;
import com.helloscala.strategy.SearchStrategy;
import com.helloscala.utils.BeanCopyUtil;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.article.ApiArticleSearchVO;
import jakarta.annotation.Resource;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("elasticsearch")
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

        logger.info("es分词搜索结果：{}", results);

        Page<ApiArticleSearchVO> resultPage = new Page<>();
        resultPage.setRecords(results);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

}
