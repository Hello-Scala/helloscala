package com.helloscala.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOArticleView;
import com.helloscala.admin.service.helper.BOArticleHelper;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.vo.category.SystemCategoryCountVO;
import com.helloscala.common.vo.system.SystemHomeDataVO;
import com.helloscala.service.entity.Article;
import com.helloscala.service.entity.SystemConfig;
import com.helloscala.service.mapper.TagMapper;
import com.helloscala.service.mapper.UserLogMapper;
import com.helloscala.service.service.ArticleService;
import com.helloscala.service.service.CategoryService;
import com.helloscala.service.service.MessageService;
import com.helloscala.service.service.RedisConstants;
import com.helloscala.service.service.RedisService;
import com.helloscala.service.service.SystemConfigService;
import com.helloscala.service.service.UserService;
import com.helloscala.service.web.request.ListArticleRequest;
import com.helloscala.service.web.request.SortingRule;
import com.helloscala.service.web.view.ArticleContributeCountView;
import com.helloscala.service.web.view.ArticleView;
import com.helloscala.service.web.view.CategoryArticleCountView;
import com.helloscala.service.web.view.CategoryView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.helloscala.service.enums.PublishEnum.PUBLISH;

@Service
@RequiredArgsConstructor
public class BOHomeService {
    private final ArticleService articleService;
    private final MessageService messageService;
    private final TagMapper tagMapper;
    private final UserLogMapper userLogMapper;
    private final SystemConfigService systemConfigService;
    private final RedisService redisService;
    private final UserService userService;
    private final CategoryService categoryService;

    // todo SiteSummaryView
    public Map<String, Integer> lineCount() {
        Map<String, Integer> map = new HashMap<>();
        map.put("article", articleService.countAll().intValue());
        map.put("message", messageService.countAll().intValue());
        map.put("user", userService.countAll().intValue());
        map.put("viewsCount", (Integer) getViewsCount());
        return map;
    }

    public SystemHomeDataVO init() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getQuantity, Article::getTitle, Article::getId)
            .eq(Article::getIsPublish, PUBLISH.getCode())
            .orderByDesc(Article::getQuantity).last("limit 6");

        SortingRule sortingRule = new SortingRule();
        sortingRule.setField("quantity");
        sortingRule.setDesc(true);
        ListArticleRequest listArticleRequest = new ListArticleRequest();
        listArticleRequest.setIsPublish(PUBLISH.getCode());
        listArticleRequest.setSortingRules(List.of(sortingRule));
        Page<ArticleView> articleViewPage = articleService.listArticleSummary(Page.of(0, 6), listArticleRequest);
        List<ArticleView> articles = articleViewPage.getRecords();

        List<BOArticleView> articleViews = articles.stream().map(BOArticleHelper::buildBoArticleView).toList();
        Map<String, Object> contribute = this.contribute();
        Map<String, Object> categoryCount = this.categoryCount();
        List<Map<String, Object>> userAccess = this.userAccess();

        List<Map<String, Object>> tagsList = tagMapper.countTags();
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        return SystemHomeDataVO.builder().dashboard(systemConfig.getDashboardNotification())
            .categoryList(categoryCount).contribute(contribute).articles(articleViews).userAccess(userAccess).tagsList(tagsList).build();
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
        List<ArticleContributeCountView> articles = articleService.contributeCount(lastTime, nowTime);
        Map<String, List<ArticleContributeCountView>> contributeMap = articles.stream().collect(Collectors.groupingBy(ArticleContributeCountView::getDate));
        months.forEach(item -> {
            List<Object> list = new ArrayList<>();
            list.add(item);
            List<ArticleContributeCountView> collect = contributeMap.getOrDefault(item, List.of());
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

        List<CategoryArticleCountView> categoryArticleCountViews = articleService.countAllCategories();
        Set<String> categoryIds = categoryArticleCountViews.stream().map(CategoryArticleCountView::getCategoryId).collect(Collectors.toSet());

        List<CategoryView> categoryViews = categoryService.listCategoryByIds(categoryIds);
        Map<String, String> cagetoryNameMap = categoryViews.stream().collect(Collectors.toMap(CategoryView::getId, CategoryView::getName));
        List<SystemCategoryCountVO> result = categoryArticleCountViews.stream().map(categoryArticleCountView -> {
            SystemCategoryCountVO systemCategoryCountVO = new SystemCategoryCountVO();
            systemCategoryCountVO.setName(cagetoryNameMap.get(categoryArticleCountView.getCategoryId()));
            systemCategoryCountVO.setValue(Optional.ofNullable(categoryArticleCountView.getCount()).map(Long::intValue).orElse(null));
            return systemCategoryCountVO;
        }).toList();
        List<String> categoryNames = cagetoryNameMap.keySet().stream().toList();
        map.put("result", result);
        map.put("categoryList", categoryNames);
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
