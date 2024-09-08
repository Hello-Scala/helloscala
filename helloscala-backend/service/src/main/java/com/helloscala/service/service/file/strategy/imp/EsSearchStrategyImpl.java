package com.helloscala.service.service.file.strategy.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.entity.ArticleElastic;
import com.helloscala.service.enums.PublishEnum;
import com.helloscala.service.esmapper.EasyesMapper;
import com.helloscala.service.service.file.strategy.ArticleSearchStrategy;
import com.helloscala.service.web.view.ArticleSummaryView;
import jakarta.annotation.Resource;
import org.dromara.easyes.common.params.SFunction;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("EsSearchStrategyImpl")
public class EsSearchStrategyImpl implements ArticleSearchStrategy {
    private static final Logger logger = LoggerFactory.getLogger(EsSearchStrategyImpl.class);
    @Resource
    private EasyesMapper easyesMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<ArticleSummaryView> searchArticle(Page<?> page, String keywords) {

        List<ArticleSummaryView> results = new ArrayList<>();

        LambdaEsQueryWrapper<ArticleElastic> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.multiMatchQuery(keywords, ArticleElastic::getTitle, ArticleElastic::getSummary)
                .eq(ArticleElastic::getIsPublish, PublishEnum.PUBLISH.getCode())
                .orderByDesc(ArticleElastic::getCreateTime);
        EsPageInfo<ArticleElastic> pageInfo = easyesMapper.pageQuery(wrapper, (int) page.getCurrent(), (int) page.getSize());

        pageInfo.getList().forEach(item -> results.add(BeanCopyUtil.copyObject(item, ArticleSummaryView.class)));
        logger.info("es search result: {}", results);

        Page<ArticleSummaryView> resultPage = new Page<>();
        resultPage.setRecords(results);
        resultPage.setTotal(pageInfo.getTotal());
        return resultPage;
    }

}
