package com.helloscala.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.utils.ListHelper;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.utils.SqlHelper;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.Article;
import com.helloscala.service.entity.ArticleTag;
import com.helloscala.service.entity.Category;
import com.helloscala.service.entity.User;
import com.helloscala.service.enums.DataEventEnum;
import com.helloscala.service.enums.PublishEnum;
import com.helloscala.service.mapper.ArticleMapper;
import com.helloscala.service.mapper.CategoryMapper;
import com.helloscala.service.service.ArticleService;
import com.helloscala.service.service.ArticleTagService;
import com.helloscala.service.service.TagService;
import com.helloscala.service.service.UserService;
import com.helloscala.service.service.event.DataEventPublisherService;
import com.helloscala.service.service.util.ArticleEntityHelper;
import com.helloscala.service.web.request.*;
import com.helloscala.service.web.view.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private final CategoryMapper categoryMapper;
    private final UserService userService;
    private final TagService tagService;
    private final RestTemplate restTemplate;
    private final ArticleTagService articleTagService;
    private final DataEventPublisherService dataEventPublisherService;
    @Value("${baidu.url}")
    private String baiduUrl;

    @Override
    public Page<ArticleView> selectArticlePage(String title, String tagId, String categoryId, Integer isPublish) {
        List<String> articleIds = articleTagService.listArticleIds(tagId);

        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.in(ObjectUtil.isNotEmpty(articleIds), Article::getId, articleIds);
        articleQuery.like(StrUtil.isNotBlank(title), Article::getTitle, title);
        articleQuery.eq(Objects.nonNull(categoryId), Article::getCategoryId, categoryId);
        articleQuery.eq(Objects.nonNull(isPublish), Article::getIsPublish, isPublish);

        Page<Article> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<Article> articlePage = baseMapper.selectPage(page, articleQuery);

        List<Article> articles = articlePage.getRecords();
        Set<String> articleIdSet = articles.stream().map(Article::getId).collect(Collectors.toSet());
        Map<String, List<TagView>> articleTagMap = fetchArticleTagMap(articleIdSet);

        Set<String> categoryIds = articles.stream().map(a -> String.valueOf(a.getCategoryId())).collect(Collectors.toSet());
        List<Category> categories = listCategoryByIds(categoryIds);
        Map<String, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<String> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return PageHelper.convertTo(articlePage, item -> {
            User user = userMap.get(item.getUserId());
            Category category = categoryMap.get(item.getCategoryId());
            List<String> tagNames = ListHelper.ofNullable(articleTagMap.get(item.getId())).stream().map(TagView::getName).toList();

            return buildArticleView(item, user, category, tagNames);
        });
    }

    @Override
    public Page<ArticleView> search(Page<?> page, SearchArticleRequest request) {

        List<String> articleIds = StrUtil.isBlank(request.getTagId()) ? List.of() : articleTagService.listArticleIds(request.getTagId());

        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.in(ObjectUtil.isNotEmpty(articleIds), Article::getId, articleIds);
        articleQuery.like(StrUtil.isNotBlank(request.getTitle()), Article::getTitle, request.getTitle());
        articleQuery.eq(Objects.nonNull(request.getCategoryId()), Article::getCategoryId, request.getCategoryId());
        articleQuery.eq(Objects.nonNull(request.getPublished()), Article::getIsPublish, request.getPublished());

        Page<Article> articlePage = baseMapper.selectPage(PageHelper.of(page), articleQuery);

        List<Article> articles = articlePage.getRecords();
        Set<String> articleIdSet = articles.stream().map(Article::getId).collect(Collectors.toSet());
        Map<String, List<TagView>> articleTagMap = fetchArticleTagMap(articleIdSet);

        Set<String> categoryIds = articles.stream().map(a -> String.valueOf(a.getCategoryId())).collect(Collectors.toSet());
        List<Category> categories = listCategoryByIds(categoryIds);
        Map<String, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<String> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return PageHelper.convertTo(articlePage, item -> {
            User user = userMap.get(item.getUserId());
            Category category = categoryMap.get(item.getCategoryId());
            List<String> tagNames = ListHelper.ofNullable(articleTagMap.get(item.getId())).stream().map(TagView::getName).toList();

            return buildArticleView(item, user, category, tagNames);
        });
    }

    private static @NotNull ArticleView buildArticleView(Article item, User user, Category category, List<String> tagNames) {
        ArticleView articleView = new ArticleView();
        articleView.setId(item.getId());
        articleView.setUserId(item.getUserId());
        articleView.setCategoryId(item.getCategoryId());
        articleView.setTitle(item.getTitle());
        if (Objects.nonNull(user)) {
            articleView.setNickname(user.getNickname());
        }
        articleView.setAvatar(item.getAvatar());
        articleView.setSummary(item.getSummary());
        articleView.setReadType(item.getReadType());
        articleView.setIsStick(item.getIsStick());
        articleView.setIsOriginal(item.getIsOriginal());
        articleView.setOriginalUrl(item.getOriginalUrl());
        articleView.setKeywords(item.getKeywords());
        articleView.setAddress(item.getAddress());
        articleView.setQuantity(item.getQuantity());
        articleView.setCreateTime(item.getCreateTime());
        articleView.setUpdateTime(item.getUpdateTime());
        articleView.setIsCarousel(item.getIsCarousel());
        articleView.setIsRecommend(item.getIsRecommend());
        articleView.setIsPublish(item.getIsPublish());
        if (Objects.nonNull(category)) {
            articleView.setCategoryName(category.getName());
        }
        articleView.setTagNames(tagNames);
        return articleView;
    }

    @Override
    public Page<ArticleView> listArticleSummary(Page<?> page, ListArticleRequest request) {
        List<String> articleIds = articleTagService.listArticleIds(request.getTagId());
        LambdaQueryWrapper<Article> articleQuery = buildQueryWrapper(request, articleIds);

        Page<Article> pageParam = new Page<>(page.getCurrent(), page.getSize());
        Page<Article> articlePage = baseMapper.selectPage(pageParam, articleQuery);

        List<Article> articles = articlePage.getRecords();
        Set<String> articleIdSet = articles.stream().map(Article::getId).collect(Collectors.toSet());
        Map<String, List<TagView>> articleTagMap = fetchArticleTagMap(articleIdSet);

        Set<String> categoryIds = articles.stream().map(a -> String.valueOf(a.getCategoryId())).collect(Collectors.toSet());
        List<Category> categories = listCategoryByIds(categoryIds);
        Map<String, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<String> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return PageHelper.convertTo(articlePage, item -> {
            User user = userMap.get(item.getUserId());
            Category category = categoryMap.get(item.getCategoryId());
            List<String> tagNames = ListHelper.ofNullable(articleTagMap.get(item.getId())).stream().map(TagView::getName).toList();
            return buildArticleView(item, user, category, tagNames);
        });
    }

    @Override
    public List<ArticleView> listArticleSummary(ListArticleRequest request) {
        List<String> articleIds = articleTagService.listArticleIds(request.getTagId());
        LambdaQueryWrapper<Article> articleQuery = buildQueryWrapper(request, articleIds);

        List<Article> articles = baseMapper.selectList(articleQuery);

        Set<String> articleIdSet = articles.stream().map(Article::getId).collect(Collectors.toSet());
        Map<String, List<TagView>> articleTagMap = fetchArticleTagMap(articleIdSet);

        Set<String> categoryIds = articles.stream().map(a -> String.valueOf(a.getCategoryId())).collect(Collectors.toSet());
        List<Category> categories = listCategoryByIds(categoryIds);
        Map<String, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<String> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return articles.stream().map(item -> {
            User user = userMap.get(item.getUserId());
            Category category = categoryMap.get(item.getCategoryId());
            List<String> tagNames = ListHelper.ofNullable(articleTagMap.get(item.getId())).stream().map(TagView::getName).toList();
            return buildArticleView(item, user, category, tagNames);
        }).toList();
    }

    @Override
    public List<ArticleView> listTopReading(Integer limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getQuantity, Article::getTitle, Article::getId)
                .eq(Article::getIsPublish, PublishEnum.PUBLISH.getCode())
                .orderByDesc(Article::getQuantity).last("limit " + limit);

        List<Article> articles = baseMapper.selectList(queryWrapper);
        return ListHelper.ofNullable(articles).stream().map(article -> buildArticleView(article, null, null, null)).toList();
    }

    @Override
    public List<BannerArticleView> searchBanner(SearchBannerArticleRequest request) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getQuantity, Article::getTitle, Article::getId)
                .eq(Objects.nonNull(request.getIsPublish()), Article::getIsPublish, request.getIsPublish())
                .eq(Objects.nonNull(request.getIsCarousel()), Article::getIsCarousel, request.getIsCarousel())
                .eq(Article::getIsCarousel, 1);

        List<SortingRule> sortingRules = request.getSortingRules();
        if (ObjectUtil.isNotEmpty(sortingRules)) {
            sortingRules.forEach(sortingRule -> {
                if (sortingRule.getDesc()) {
                    queryWrapper.orderByDesc(SqlHelper.toFieldFunc(Article.class, sortingRule.getField()));
                } else {
                    queryWrapper.orderByAsc(SqlHelper.toFieldFunc(Article.class, sortingRule.getField()));
                }
            });
        } else {
            queryWrapper.orderByDesc(Article::getCreateTime);
        }
        

        List<Article> articles = baseMapper.selectList(queryWrapper);

        return ListHelper.ofNullable(articles).stream().map(article -> {
            BannerArticleView bannerArticleView = new BannerArticleView();
            bannerArticleView.setId(article.getId());
            bannerArticleView.setTitle(article.getTitle());
            bannerArticleView.setAvatar(article.getAvatar());
            bannerArticleView.setCreateTime(article.getCreateTime());
            return bannerArticleView;
        }).toList();

    }

    @NotNull
    private static LambdaQueryWrapper<Article> buildQueryWrapper(ListArticleRequest request, List<String> articleIds) {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(StrUtil.isNotBlank(request.getUserId()), Article::getUserId, request.getUserId());
        articleQuery.eq(Objects.nonNull(request.getReadType()), Article::getReadType, request.getReadType());
        articleQuery.in(ObjectUtil.isNotEmpty(articleIds), Article::getId, articleIds);
        articleQuery.like(StrUtil.isNotBlank(request.getTitle()), Article::getTitle, request.getTitle());
        articleQuery.eq(Objects.nonNull(request.getCategoryId()), Article::getCategoryId, request.getCategoryId());
        articleQuery.eq(Objects.nonNull(request.getIsPublish()), Article::getIsPublish, request.getIsPublish());
        if (ObjectUtil.isNotEmpty(request.sortingRules)) {
            request.getSortingRules().forEach(sortingRule -> {
                if (sortingRule.getDesc()) {
                    articleQuery.orderByDesc(SqlHelper.toFieldFunc(Article.class, sortingRule.getField()));
                } else {
                    articleQuery.orderByAsc(SqlHelper.toFieldFunc(Article.class, sortingRule.getField()));
                }
            });
        } else {
            articleQuery.orderByDesc(Article::getCreateTime);
        }
        return articleQuery;
    }

    @Override
    public Page<ArticleDetailView> listArticleDetail(Page<?> page, ListArticleRequest request) {
        List<String> articleIds = articleTagService.listArticleIds(request.getTagId());

        LambdaQueryWrapper<Article> articleQuery = buildQueryWrapper(request, articleIds);

        Page<Article> pageParam = new Page<>(page.getCurrent(), page.getSize());
        Page<Article> articlePage = baseMapper.selectPage(pageParam, articleQuery);

        List<Article> articles = articlePage.getRecords();
        Set<String> articleIdSet = articles.stream().map(Article::getId).collect(Collectors.toSet());
        Map<String, List<TagView>> articleTagMap = fetchArticleTagMap(articleIdSet);

        Set<String> categoryIds = articles.stream().map(a -> String.valueOf(a.getCategoryId())).collect(Collectors.toSet());
        List<Category> categories = listCategoryByIds(categoryIds);
        Map<String, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<String> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return PageHelper.convertTo(articlePage, article -> {
            User user = userMap.get(article.getUserId());
            Category category = categoryMap.get(article.getCategoryId());
            List<TagView> tagViews = ListHelper.ofNullable(articleTagMap.get(article.getId())).stream().map(tag -> {
                TagView tagView = new TagView();
                tagView.setId(tag.getId());
                tagView.setName(tag.getName());
                return tagView;
            }).toList();
            return buildArticleDetailView(article, user, category, tagViews);
        });
    }

    private static @NotNull ArticleDetailView buildArticleDetailView(Article article, User user, Category category, List<TagView> tagViews) {
        ArticleDetailView articleDetailView = new ArticleDetailView();
        articleDetailView.setId(article.getId());
        articleDetailView.setTitle(article.getTitle());
        if (Objects.nonNull(user)) {
            articleDetailView.setUserId(user.getId());
            articleDetailView.setUserAvatar(user.getAvatar());
            articleDetailView.setUserNickname(user.getNickname());
        }
        articleDetailView.setAvatar(article.getAvatar());
        articleDetailView.setSummary(article.getSummary());
        articleDetailView.setQuantity(article.getQuantity());
        articleDetailView.setContent(article.getContent());
        articleDetailView.setContentMd(article.getContentMd());
        articleDetailView.setKeywords(article.getKeywords());
        articleDetailView.setReadType(article.getReadType());
        articleDetailView.setIsStick(article.getIsStick());
        articleDetailView.setIsOriginal(article.getIsOriginal());
        articleDetailView.setOriginalUrl(article.getOriginalUrl());
        if (Objects.nonNull(category)) {
            articleDetailView.setCategoryId(category.getId());
            articleDetailView.setCategoryName(category.getName());
        }
        articleDetailView.setIsPublish(article.getIsPublish());
        articleDetailView.setIsCarousel(article.getIsCarousel());
        articleDetailView.setIsRecommend(article.getIsRecommend());
        articleDetailView.setTags(tagViews);
        articleDetailView.setCreateTime(article.getCreateTime());
        articleDetailView.setUpdateTime(article.getUpdateTime());
        return articleDetailView;
    }

    @Override
    public List<ArticleDetailView> listArticleDetailByIds(Set<String> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return List.of();
        }
        List<Article> articles = baseMapper.selectBatchIds(ids);
        Set<String> articleIdSet = articles.stream().map(Article::getId).collect(Collectors.toSet());
        Map<String, List<TagView>> articleTagMap = fetchArticleTagMap(articleIdSet);

        Set<String> categoryIds = articles.stream().map(a -> String.valueOf(a.getCategoryId())).collect(Collectors.toSet());
        List<Category> categories = listCategoryByIds(categoryIds);
        Map<String, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<String> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return articles.stream().map(article -> {
            User user = userMap.get(article.getUserId());
            Category category = categoryMap.get(article.getCategoryId());
            List<TagView> tagViews = ListHelper.ofNullable(articleTagMap.get(article.getId())).stream().map(tag -> {
                TagView tagView = new TagView();
                tagView.setId(tag.getId());
                tagView.setName(tag.getName());
                return tagView;
            }).toList();
            return buildArticleDetailView(article, user, category, tagViews);
        }).toList();
    }

    @NotNull
    private Map<String, List<TagView>> fetchArticleTagMap(Set<String> articleIdSet) {
        List<ArticleTag> articleTags = articleTagService.listByArticleIds(articleIdSet);
        Map<String, List<ArticleTag>> articleTagMap = articleTags.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        Set<String> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toSet());
        List<TagView> tags = tagService.listTagByIds(tagIds);
        Map<String, TagView> tagMap = tags.stream().collect(Collectors.toMap(TagView::getId, Function.identity()));
        Map<String, List<TagView>> result = new HashMap<>();
        articleTagMap.forEach((articleId, tagList) -> {
            List<TagView> articleTagList = tagList.stream().map(at -> tagMap.get(at.getTagId())).filter(Objects::nonNull).toList();
            result.put(articleId, articleTagList);
        });
        return result;
    }

    @Override
    public ArticleDetailView getDetailById(String id) {
        Article article = getById(id);
        if (Objects.isNull(article)) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        User user = userService.getById(article.getUserId());
        Category category = categoryMapper.selectById(article.getCategoryId());
        Map<String, List<TagView>> articleTagMap = fetchArticleTagMap(Set.of(id));
        List<TagView> tagViews = ListHelper.ofNullable(articleTagMap.get(id)).stream().map(tag -> {
            TagView tagView = new TagView();
            tagView.setId(tag.getId());
            tagView.setName(tag.getName());
            return tagView;
        }).toList();
        return buildArticleDetailView(article, user, category, tagViews);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(String userId, String ipAddress, CreateArticleRequest request) {
        String categoryId = getOrCreateCategory(request.getCategoryId(), request.getCategoryName());

        Set<String> tagNames = new HashSet<>(request.getTags());
        List<TagView> tags = ObjectUtil.isNotEmpty(request.getTagIds()) ? tagService.listTagByIds(new HashSet<>(request.getTagIds())) : tagService.listByNames(tagNames);
        Set<String> existTagNames = tags.stream().map(TagView::getName).collect(Collectors.toSet());
        Set<String> tagNameToCreateList = tagNames.stream().filter(n -> !existTagNames.contains(n)).collect(Collectors.toSet());
        List<TagView> tagsCreated = tagService.bulkCreateByNames(tagNameToCreateList);
        List<TagView> articleTags = ListHelper.concat(tags, tagsCreated);

        Article article = new Article();
        article.setUserId(userId);
        article.setCategoryId(categoryId);
        article.setTitle(request.getTitle());
        article.setAvatar(request.getAvatar());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setContentMd(request.getContentMd());
        article.setIsPublish(request.getIsPublish());
        article.setReadType(request.getReadType());
        article.setIsStick(request.getIsStick());
        article.setIsOriginal(request.getIsOriginal());
        article.setOriginalUrl(request.getOriginalUrl());
        article.setKeywords(request.getKeywords());
        article.setQuantity(request.getQuantity());
        article.setIsCarousel(request.getIsCarousel());
        article.setIsRecommend(request.getIsRecommend());
        article.setUpdateTime(new Date());

        if (StrUtil.isNotBlank(ipAddress)
                && !"UNKNOWN".equals(ipAddress)
                && ipAddress.split("\\|").length > 1) {
            String[] split = ipAddress.split("\\|");
            String address = split[1];
            article.setAddress(address);
        } else {
            article.setAddress("Earth");
        }
        int insert = baseMapper.insert(article);
        if (insert <= 0) {
            throw new ConflictException("Create article failed, article={}", JSONObject.toJSON(article));
        }
        Set<String> tagIds = articleTags.stream().map(TagView::getId).collect(Collectors.toSet());
        articleTagService.resetArticleTags(article.getId(), tagIds);

        //发布消息去同步es 不进行判断是否是发布状态了，因为后面修改成下架的话就还得去删除es里面的数据，多次一举了，在查询时添加条件发布状态为已发布
        dataEventPublisherService.publishData(DataEventEnum.ES_ADD_ARTICLE, ArticleEntityHelper.toElasticEntity(article));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(String userId, String articleId, UpdateArticleRequest request) {
        Article article = getById(articleId);
        if (Objects.isNull(article)) {
            throw new NotFoundException("Article not found, articleId={}!", articleId);
        }
        String categoryId = getOrCreateCategory(request.getCategoryId(), request.getCategoryName());

        Set<String> tagNames = new HashSet<>(request.getTags());
        List<TagView> tags = ObjectUtil.isNotEmpty(request.getTagIds()) ? tagService.listTagByIds(new HashSet<>(request.getTagIds())) : tagService.listByNames(tagNames);

        Set<String> existTagNames = tags.stream().map(TagView::getName).collect(Collectors.toSet());
        Set<String> tagNameToCreateList = tagNames.stream().filter(n -> !existTagNames.contains(n)).collect(Collectors.toSet());
        List<TagView> tagsCreated = tagService.bulkCreateByNames(tagNameToCreateList);
        List<TagView> articleTags = ListHelper.concat(tags, tagsCreated);
        Set<String> newTagIds = articleTags.stream().map(TagView::getId).collect(Collectors.toSet());

        article.setUserId(userId);
        article.setCategoryId(categoryId);
        article.setTitle(request.getTitle());
        article.setAvatar(request.getAvatar());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setContentMd(request.getContentMd());
        article.setIsPublish(request.getIsPublish());
        article.setReadType(request.getReadType());
        article.setIsStick(request.getIsStick());
        article.setIsOriginal(request.getIsOriginal());
        article.setOriginalUrl(request.getOriginalUrl());
        article.setKeywords(request.getKeywords());
        article.setQuantity(request.getQuantity());
        article.setIsCarousel(request.getIsCarousel());
        article.setIsRecommend(request.getIsRecommend());
        article.setUpdateTime(new Date());

        int update = baseMapper.updateById(article);
        if (update <= 0) {
            throw new ConflictException("Update article failed, article={}", JSONObject.toJSON(article));
        }
        articleTagService.resetArticleTags(article.getId(), newTagIds);
        dataEventPublisherService.publishData(DataEventEnum.ES_UPDATE_ARTICLE, ArticleEntityHelper.toElasticEntity(article));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchArticle(Set<String> ids) {
        baseMapper.deleteBatchIds(ids);
        articleTagService.deleteByArticleIds(new HashSet<>(ids));
        dataEventPublisherService.publishData(DataEventEnum.ES_DELETE_ARTICLE, ids);
    }

    @Override
    public int stick(String id, boolean stick) {
        LambdaUpdateWrapper<Article> articleUpdateWrapper = new LambdaUpdateWrapper<>();
        articleUpdateWrapper.eq(Article::getId, id);
        articleUpdateWrapper.set(Article::getIsStick, stick ? 0 : 1);
        return baseMapper.update(articleUpdateWrapper);
    }

    @Override
    public void seoArticle(List<String> ids) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "data.zz.baidu.com");
        headers.add("User-Agent", "curl/7.12.1");
        headers.add("Content-Length", "83");
        headers.add("Content-Type", "text/plain");

        ids.forEach(item -> {
            String url = "http://www.helloscala.com/article/" + item;
            HttpEntity<String> entity = new HttpEntity<>(url, headers);
            restTemplate.postForObject(baiduUrl, entity, String.class);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void psArticle(Article article) {
        baseMapper.updateById(article);
    }

    @Override
    public boolean existArticle(String id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    public boolean ownArticle(String userId, String id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        queryWrapper.eq(Article::getUserId, userId);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    public boolean ownArticles(String userId, Set<String> ids) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getUserId);
        queryWrapper.in(Article::getId, ids);
        queryWrapper.eq(Article::getUserId, userId);
        List<Article> userArticles = baseMapper.selectList(queryWrapper);

        Set<String> userOwnedArticleIds = userArticles.stream().map(Article::getId)
                .collect(Collectors.toSet());
        return userOwnedArticleIds.containsAll(ids);
    }

    @Override
    public boolean existUnderCategory(String categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCategoryId, categoryId);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    public boolean existAnyUnderCategory(Set<String> categoryIds) {
        if (ObjectUtil.isEmpty(categoryIds)) {
            return false;
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Article::getCategoryId, categoryIds);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    public List<CategoryArticleCountView> countByCategories(Set<String> categoryIds) {
        if (ObjectUtil.isEmpty(categoryIds)) {
            return List.of();
        }
        return countArticleByCategories(categoryIds);
    }

    private @NotNull List<CategoryArticleCountView> countArticleByCategories(Set<String> categoryIds) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getCategoryId);
        queryWrapper.in(ObjectUtil.isNotEmpty(categoryIds), Article::getCategoryId, categoryIds);
        List<Article> articleList = baseMapper.selectList(queryWrapper);
        Map<String, List<Article>> articleMap = articleList.stream().collect(Collectors.groupingBy(Article::getCategoryId));
        List<Category> categoryViews = listCategoryByIds(categoryIds);
        Map<String, Category> categoryViewMap = categoryViews.stream().collect(Collectors.toMap(Category::getId, Function.identity()));
        return articleMap.entrySet().stream().map(entry -> {
            Category categoryView = categoryViewMap.get(entry.getKey());
            CategoryArticleCountView countView = new CategoryArticleCountView();
            countView.setId(entry.getKey());
            countView.setName(countView.getName());
            countView.setIcon(categoryView.getIcon());
            countView.setCount(entry.getValue().size());
            return countView;
        }).toList();
    }

    private List<Category> listCategoryByIds(Set<String> categoryIds) {
        return categoryIds.isEmpty() ? List.of() : categoryMapper.selectBatchIds(categoryIds);
    }

    @Override
    public List<CategoryArticleCountView> countAllCategories() {
        return countArticleByCategories(new HashSet<>());
    }

    @Override
    public Long countAll() {
        return baseMapper.selectCount(null);
    }

    @Override
    public List<ArticleContributeCountView> contributeCount(String startTime, String endTime) {
        return baseMapper.contribute(startTime, endTime);
    }

    private String getOrCreateCategory(String categoryId, String categoryName) {
        LambdaQueryWrapper<Category> categoryQuery = new LambdaQueryWrapper<>();
        categoryQuery.eq(StrUtil.isNotBlank(categoryId), Category::getId, categoryId)
                .or()
                .eq(Category::getName, categoryName)
                .last(SqlHelper.LIMIT_1);
        Category category = categoryMapper.selectOne(categoryQuery);
        if (Objects.nonNull(category)) {
            return category.getId();
        }
        category = Category.builder().name(categoryName).sort(0).build();
        int insert = categoryMapper.insert(category);
        if (insert <= 0) {
            throw new ConflictException("Failed to create category, category={}!", category);
        }
        return category.getId();
    }
}
