package com.helloscala.service.service.file.strategy.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.Constants;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.mapper.ArticleMapper;
import com.helloscala.service.service.file.strategy.ArticleSearchStrategy;
import com.helloscala.service.web.view.ArticleSummaryView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service("mysqlSearch")
@RequiredArgsConstructor
public class MysqlSearchStrategyImpl implements ArticleSearchStrategy {

    private final ArticleMapper articleMapper;

    @Override
    public Page<ArticleSummaryView> searchArticle(Page<?> page, String keywords){
        Page<ArticleSummaryView> articlePage = articleMapper.selectSearchArticle(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                keywords);
        articlePage.getRecords().forEach(item -> item.setTitle(item.getTitle().replaceAll(keywords, Constants.PRE_TAG + keywords + Constants.POST_TAG)));
        return articlePage;
    }
}
