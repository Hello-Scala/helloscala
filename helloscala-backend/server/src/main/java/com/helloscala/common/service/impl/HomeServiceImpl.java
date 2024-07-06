package com.helloscala.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.common.RedisConstants;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.SystemConfig;
import com.helloscala.common.mapper.*;
import com.helloscala.common.service.RedisService;
import com.helloscala.common.service.SystemConfigService;
import com.helloscala.common.vo.article.SystemArticleContributeVO;
import com.helloscala.common.vo.category.SystemCategoryCountVO;
import com.helloscala.common.vo.system.SystemHomeDataVO;
import com.helloscala.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.helloscala.common.enums.PublishEnum.PUBLISH;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl {
    private final ArticleMapper articleMapper;
    private final MessageMapper messageMapper;
    private final TagMapper tagMapper;
    private final CategoryMapper categoryMapper;
    private final UserLogMapper userLogMapper;
    private final SystemConfigService systemConfigService;
    private final RedisService redisService;
    private final UserMapper userMapper;

    // todo SiteSummaryView
    public Map<String, Integer> lineCount() {
        Map<String, Integer> map = new HashMap<>();
        map.put("article", articleMapper.selectCount(null).intValue());
        map.put("message", messageMapper.selectCount(null).intValue());
        map.put("user", userMapper.selectCount(null).intValue());
        map.put("viewsCount", (Integer) getViewsCount());
        return map;
    }

    public SystemHomeDataVO init() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getQuantity, Article::getTitle, Article::getId)
                .eq(Article::getIsPublish, PUBLISH.getCode())
                .orderByDesc(Article::getQuantity).last("limit 6");

        List<Article> articles = articleMapper.selectList(queryWrapper);
        Map<String, Object> contribute = this.contribute();
        Map<String, Object> categoryCount = this.categoryCount();
        List<Map<String, Object>> userAccess = this.userAccess();

        List<Map<String, Object>> tagsList = tagMapper.countTags();
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        return SystemHomeDataVO.builder().dashboard(systemConfig.getDashboardNotification())
                .categoryList(categoryCount).contribute(contribute).articles(articles).userAccess(userAccess).tagsList(tagsList).build();
    }

    public static List<String> getMonths() {
        List<String> dateList = new ArrayList<String>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        dateList.add(DateUtil.formateDate(calendar.getTime(), DateUtil.YYYY_MM_DD));
        while (date.after(calendar.getTime())) { //倒序时间,顺序after改before其他相应的改动。
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(DateUtil.formateDate(calendar.getTime(), DateUtil.YYYY_MM_DD));
        }
        return dateList;
    }

    public Map<String, Object> contribute() {
        Map<String, Object> map = new HashMap<>();
        List<Object> contributes = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        List<String> months = getMonths();
        String lastTime = months.get(0), nowTime = months.get(months.size() - 1);
        List<SystemArticleContributeVO> articles = articleMapper.contribute(lastTime, nowTime);
        months.forEach(item -> {
            List<Object> list = new ArrayList<>();
            list.add(item);
            List<SystemArticleContributeVO> collect = articles.stream().filter(article -> article.getDate().equals(item)).toList();
            if (!collect.isEmpty()) {
                list.add(collect.get(0).getCount());
            } else {
                list.add(0);
            }
            result.add(list);
        });
        contributes.add(lastTime);
        contributes.add(nowTime);
        map.put("contributeDate", contributes);
        map.put("blogContributeCount", result);
        return map;
    }

    public Map<String, Object> categoryCount() {
        Map<String, Object> map = new HashMap<>();
        List<SystemCategoryCountVO> result = categoryMapper.countArticle();
        List<String> list = new ArrayList<>();
        result.forEach(item -> list.add(item.getName()));
        map.put("result", result);
        map.put("categoryList", list);
        return map;
    }

    public List<Map<String, Object>> userAccess() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
        return userLogMapper.getUserAccess(DateUtil.formateDate(calendar.getTime(), DateUtil.YYYY_MM_DD));
    }

    private Object getViewsCount() {
        Object count = redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT);
        return Optional.ofNullable(count).orElse(0);
    }

}
