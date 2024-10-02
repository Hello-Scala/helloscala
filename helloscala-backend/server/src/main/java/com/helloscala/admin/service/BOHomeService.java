package com.helloscala.admin.service;

import com.helloscala.admin.controller.view.BOArticleView;
import com.helloscala.admin.controller.view.BOCategoryArticleCountView;
import com.helloscala.admin.controller.view.BOTagView;
import com.helloscala.admin.controller.view.BOUserIPCountView;
import com.helloscala.admin.service.helper.BOArticleHelper;
import com.helloscala.admin.service.helper.RedisConstants;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.vo.system.SystemHomeDataVO;
import com.helloscala.service.mapper.TagMapper;
import com.helloscala.service.service.*;
import com.helloscala.service.web.view.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOHomeService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ArticleService articleService;
    private final MessageService messageService;
    private final UserService userService;
    private final UserLogService userLogService;
    private final TagService tagService;
    private final TagMapper tagMapper;
    private final SystemConfigService systemConfigService;

    // todo SiteSummaryView
    public Map<String, Integer> lineCount() {
        Map<String, Integer> map = new HashMap<>();
        map.put("article", articleService.countAll().intValue());
        map.put("message", messageService.countAll());
        map.put("user", userService.countAll().intValue());
        map.put("viewsCount", getViewsCount());
        return map;
    }

    public SystemHomeDataVO init() {
        List<ArticleView> articles = articleService.listTopReading(6);
        List<BOArticleView> articleViews = articles.stream().map(BOArticleHelper::buildBoArticleView).toList();
        Map<String, Object> contribute = contribute();
        Map<String, Object> categoryCount = categoryCount();
        List<BOUserIPCountView> boUserIPCountViews = userAccess();

        List<TagView> tagList = tagService.listAllTags();
        List<BOTagView> tagViews = tagList.stream().map(tag -> {
            BOTagView tagView = new BOTagView();
            tagView.setId(tag.getId());
            tagView.setName(tag.getName());
            return tagView;
        }).toList();

        SystemConfigView systemConfig = systemConfigService.getCustomizeOne();
        return SystemHomeDataVO.builder().dashboard(systemConfig.getDashboardNotification())
                .categoryList(categoryCount)
                .contribute(contribute)
                .articles(articleViews)
                .userAccess(boUserIPCountViews)
                .tagsList(tagViews).build();
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
        List<ArticleContributeCountView> contributeCountViews = articleService.contributeCount(lastTime, nowTime);

        months.forEach(item -> {
            List<Object> list = new ArrayList<>();
            list.add(item);
            List<ArticleContributeCountView> collect = contributeCountViews.stream().filter(article -> article.getDate().equals(item)).toList();
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
        List<CategoryArticleCountView> categoryArticleCountViews = articleService.countAllCategories();
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        categoryArticleCountViews.forEach(item -> list.add(item.getName()));
        List<BOCategoryArticleCountView> countViews = categoryArticleCountViews.stream().map(count -> {
            BOCategoryArticleCountView countView = new BOCategoryArticleCountView();
            countView.setId(count.getId());
            countView.setName(countView.getName());
            countView.setCount(count.getCount());
            return countView;
        }).toList();
        map.put("result", countViews);
        map.put("categoryList", list);
        return map;
    }

    public List<BOUserIPCountView> userAccess() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
        List<UserIPCountView> userIPCountViews = userLogService.countIP(DateUtil.formateDate(calendar.getTime(), DateUtil.YYYY_MM_DD));
        return userIPCountViews.stream().map(count -> {
            BOUserIPCountView countView = new BOUserIPCountView();
            countView.setIp(count.getIp());
            countView.setAccess(count.getCount());
            countView.setCreateTime(count.getCreateDate());
            return countView;
        }).toList();
    }

    private Integer getViewsCount() {
        Object count = redisTemplate.opsForValue().get(RedisConstants.BLOG_VIEWS_COUNT);
        return (Integer) Optional.ofNullable(count).orElse(0);
    }
}
