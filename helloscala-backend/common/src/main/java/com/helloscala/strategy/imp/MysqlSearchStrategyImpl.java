package com.helloscala.strategy.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.Constants;
import com.helloscala.mapper.ArticleMapper;
import com.helloscala.strategy.SearchStrategy;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.article.ApiArticleSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service("mysqlSearch")
@RequiredArgsConstructor
public class MysqlSearchStrategyImpl implements SearchStrategy {

    private final ArticleMapper articleMapper;

    @Override
    public Page<ApiArticleSearchVO> searchArticle(String keywords){
        // 搜索文章
        Page<ApiArticleSearchVO> articlePage = articleMapper.selectSearchArticle(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                keywords);
        //高亮处理
        articlePage.getRecords().forEach(item -> item.setTitle(item.getTitle().replaceAll(keywords, Constants.PRE_TAG + keywords + Constants.POST_TAG)));

        return articlePage;
    }
}
