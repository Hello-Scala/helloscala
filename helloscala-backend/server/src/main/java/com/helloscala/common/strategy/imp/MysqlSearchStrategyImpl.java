package com.helloscala.common.strategy.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.Constants;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.strategy.SearchStrategy;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service("mysqlSearch")
@RequiredArgsConstructor
public class MysqlSearchStrategyImpl implements SearchStrategy {

    private final ArticleMapper articleMapper;

    @Override
    public Page<ApiArticleSearchVO> searchArticle(String keywords){
        Page<ApiArticleSearchVO> articlePage = articleMapper.selectSearchArticle(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                keywords);
        articlePage.getRecords().forEach(item -> item.setTitle(item.getTitle().replaceAll(keywords, Constants.PRE_TAG + keywords + Constants.POST_TAG)));
        return articlePage;
    }
}
