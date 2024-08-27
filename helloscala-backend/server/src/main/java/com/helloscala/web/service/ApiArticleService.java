package com.helloscala.web.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOArticleCategoryView;
import com.helloscala.admin.controller.view.BOTagView;
import com.helloscala.common.dto.article.ArticlePostDTO;
import com.helloscala.common.strategy.context.SearchStrategyContext;
import com.helloscala.common.utils.*;
import com.helloscala.common.vo.article.*;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.*;
import com.helloscala.service.enums.ReadTypeEnum;
import com.helloscala.service.enums.SearchModelEnum;
import com.helloscala.service.mapper.ArticleMapper;
import com.helloscala.service.mapper.CollectMapper;
import com.helloscala.service.mapper.FollowedMapper;
import com.helloscala.service.service.*;
import com.helloscala.service.web.request.CreateArticleRequest;
import com.helloscala.service.web.request.ListArticleRequest;
import com.helloscala.service.web.request.SortingRule;
import com.helloscala.service.web.request.UpdateArticleRequest;
import com.helloscala.service.web.view.ArticleDetailView;
import com.helloscala.service.web.view.ArticleView;
import com.helloscala.service.web.view.CollectCountView;
import com.helloscala.service.web.view.CommentView;
import com.helloscala.web.controller.article.request.APICreateArticleRequest;
import com.helloscala.web.controller.article.request.APIUpdateArticleRequest;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.response.ListPublishedArticleResponse;
import com.helloscala.web.response.PublishedArticleView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.helloscala.common.ResultCode.ERROR_EXCEPTION_MOBILE_CODE;
import static com.helloscala.common.ResultCode.PARAMS_ILLEGAL;
import static com.helloscala.service.service.RedisConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiArticleService {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final RedisService redisService;
    private final TagService tagService;
    private final CommentService commentService;
    private final CollectMapper collectMapper;
    private final CollectService collectService;
    private final FollowedService followedService;
    private final FollowedMapper followedMapper;
    private final ArticleTagService articleTagService;
    private final SystemConfigService systemConfigService;
    private final SearchStrategyContext searchStrategyContext;


    public Page<RecommendedArticleVO> selectArticleList(String userId, String categoryId, String tagId, String orderByDescColumn) {
        Page<?> page = PageUtil.getPage();

        SortingRule sortingRule = new SortingRule();
        sortingRule.setDesc(true);
        sortingRule.setField("isStick");
        SortingRule orderByDescColumnSorting = new SortingRule();
        orderByDescColumnSorting.setDesc(true);
        orderByDescColumnSorting.setField(orderByDescColumn);
        List<SortingRule> sortingRules = List.of(sortingRule, orderByDescColumnSorting);

        ListArticleRequest listArticleRequest = new ListArticleRequest();
        listArticleRequest.setIsPublish(1);
        listArticleRequest.setCategoryId(categoryId);
        listArticleRequest.setTagId(tagId);
        listArticleRequest.setSortingRules(sortingRules);
        return searchRecommendedArticlePage(userId, page, listArticleRequest);
    }

    @NotNull
    private Page<RecommendedArticleVO> searchRecommendedArticlePage(String userId, Page<?> page, ListArticleRequest listArticleRequest) {
        Page<ArticleDetailView> articleViewPage = articleService.listArticleDetail(page, listArticleRequest);
        List<ArticleDetailView> records = articleViewPage.getRecords();

        Set<String> articleIdSet = records.stream().map(ArticleDetailView::getId).collect(Collectors.toSet());

        List<CollectCountView> articleCollectCounts = collectService.countByArticles(articleIdSet);
        Map<String, Long> collectCountMap = articleCollectCounts.stream().collect(Collectors.toMap(CollectCountView::getArticleId, CollectCountView::getCount));

        Map<String, List<Tag>> articleTagListMap = getArticleTagListMap(articleIdSet);

        List<CommentView> comments = commentService.listArticleComment(articleIdSet);
        Map<String, List<CommentView>> commentMap = comments.stream().collect(Collectors.groupingBy(CommentView::getArticleId));

        List<String> collectArticleIds = collectService.listCollectArticleIds(userId);
        Map<String, Object> articleLikeCountMap = MapHelper.ofNullable(redisService.getCacheMap(ARTICLE_LIKE_COUNT));

        return PageHelper.convertTo(articleViewPage, articleDetailView -> {
            List<CommentView> articleComments = commentMap.getOrDefault(articleDetailView.getId(), List.of());
            List<Tag> tagList = articleTagListMap.getOrDefault(articleDetailView.getId(), List.of());
            Integer likeCount = (Integer) articleLikeCountMap.getOrDefault(articleDetailView.getId(), 0);
            Long collectCount = collectCountMap.getOrDefault(articleDetailView.getId(), 0L);
            List<BOTagView> tagViews = tagList.stream().map(tag -> {
                BOTagView tagView = new BOTagView();
                tagView.setId(tag.getId());
                tagView.setName(tag.getName());
                return tagView;
            }).toList();
            RecommendedArticleVO recommendedArticleVO = new RecommendedArticleVO();
            recommendedArticleVO.setId(articleDetailView.getId());
            recommendedArticleVO.setNickname(articleDetailView.getUserNickname());
            recommendedArticleVO.setUserAvatar(articleDetailView.getUserAvatar());
            recommendedArticleVO.setUserId(articleDetailView.getUserId());
            recommendedArticleVO.setTitle(articleDetailView.getTitle());
            recommendedArticleVO.setAvatar(articleDetailView.getAvatar());
            recommendedArticleVO.setSummary(articleDetailView.getSummary());
            recommendedArticleVO.setContent(articleDetailView.getContent());
            recommendedArticleVO.setIsStick(articleDetailView.getIsStick());
            recommendedArticleVO.setIsOriginal(articleDetailView.getIsOriginal());
            recommendedArticleVO.setIsPublish(articleDetailView.getIsPublish());
            recommendedArticleVO.setQuantity(articleDetailView.getQuantity());
            recommendedArticleVO.setCommentCount(articleComments.size());
            recommendedArticleVO.setLikeCount(likeCount);
            recommendedArticleVO.setCollectCount(collectCount.intValue());
            recommendedArticleVO.setIsCollect(collectArticleIds.contains(articleDetailView.getId()));
            recommendedArticleVO.setCategoryName(articleDetailView.getCategoryName());
            recommendedArticleVO.setCategoryId(articleDetailView.getCategoryId());
            recommendedArticleVO.setCreateTime(articleDetailView.getCreateTime());
            recommendedArticleVO.setFormatCreateTime(RelativeDateFormat.format(articleDetailView.getCreateTime()));
            recommendedArticleVO.setTagList(tagViews);
            return recommendedArticleVO;
        });
    }

    @NotNull

    public Map<String, List<Tag>> getArticleTagListMap(Set<String> articleIdSet) {
        if (ObjectUtil.isEmpty(articleIdSet)) {
            return Map.of();
        }
        List<ArticleTag> articleTags = articleTagService.listByArticleIds(articleIdSet);
        Map<String, List<ArticleTag>> articleTagMap = articleTags.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        Set<String> tagIdSet = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toSet());
        List<Tag> tags = tagService.listByIds(tagIdSet);
        Map<String, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, Function.identity()));
        return articleTagMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(at -> tagMap.get(at.getTagId())).filter(Objects::nonNull).toList()));
    }


    public ArticleInfoVO selectArticleInfo(String userId, String id, String ip) {
        ArticleDetailView articleDetailView = articleService.getDetailById(id);
        if (Objects.isNull(articleDetailView)) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        Long commentCount = commentService.countByArticleId(id);
        List<Object> cacheList = redisService.getCacheList(RedisConstants.CHECK_CODE_IP);

        List<CollectCountView> articleCollectCounts = collectService.countByArticles(Set.of(id));
        Map<String, Long> collectCountMap = articleCollectCounts.stream().collect(Collectors.toMap(CollectCountView::getArticleId, CollectCountView::getCount));

        List<String> followedUserIds = followedService.listFollowedUserIds(userId);

        Long userCommentCount = commentService.countByUserId(userId);

        List<String> collectArticleIds = collectService.listCollectArticleIds(userId);
        Map<String, Object> articleLikeCountMap = MapHelper.ofNullable(redisService.getCacheMap(ARTICLE_LIKE_COUNT));
        String articleLikeKey = ARTICLE_USER_LIKE + userId;
        boolean like = Optional.ofNullable(redisService.sIsMember(articleLikeKey, id)).orElse(false);

        boolean activeReadeType = like && articleDetailView.getReadType() == ReadTypeEnum.LIKE.index
                || articleDetailView.getReadType() == ReadTypeEnum.COMMENT.index && Optional.ofNullable(userCommentCount).orElse(0L) > 0L
                || articleDetailView.getReadType() == ReadTypeEnum.CODE.index && cacheList.contains(ip);

        BOArticleCategoryView boCategoryView = new BOArticleCategoryView();
        boCategoryView.setId(articleDetailView.getCategoryId());
        boCategoryView.setName(articleDetailView.getCategoryName());

        List<BOTagView> tagViews = articleDetailView.getTags().stream().map(tagView -> {
            BOTagView boTagView = new BOTagView();
            boTagView.setId(tagView.getId());
            boTagView.setName(tagView.getName());
            return boTagView;
        }).toList();

        redisService.incrArticle(id, ARTICLE_READING, IpUtil.getIp());

        ArticleInfoVO articleInfoVO = new ArticleInfoVO();
        articleInfoVO.setId(articleDetailView.getId());
        articleInfoVO.setNickname(articleDetailView.getUserNickname());
        articleInfoVO.setAvatar(articleDetailView.getUserAvatar());
        articleInfoVO.setUserId(articleDetailView.getUserId());
        articleInfoVO.setTitle(articleDetailView.getTitle());
        articleInfoVO.setAvatar(articleDetailView.getAvatar());
        articleInfoVO.setSummary(articleDetailView.getSummary());
        articleInfoVO.setContent(articleDetailView.getContent());
        articleInfoVO.setIsStick(articleDetailView.getIsStick());
        articleInfoVO.setIsOriginal(articleDetailView.getIsOriginal());
        articleInfoVO.setIsPublish(articleDetailView.getIsPublish());
        articleInfoVO.setQuantity(articleDetailView.getQuantity());
        articleInfoVO.setCommentCount(commentCount);
        articleInfoVO.setLikeCount(articleLikeCountMap.getOrDefault(id, 0));
        articleInfoVO.setCollectCount(collectCountMap.getOrDefault(id, 0L).intValue());
        articleInfoVO.setIsCollect(collectArticleIds.contains(id) ? 1 : 0);
        articleInfoVO.setCategory(boCategoryView);
        articleInfoVO.setCreateTime(articleDetailView.getCreateTime());
        articleInfoVO.setUpdateTime(articleDetailView.getUpdateTime());
        articleInfoVO.setTagList(tagViews);
        articleInfoVO.setIsFollowed(followedUserIds.contains(articleDetailView.getUserId()) ? 1 : 0);
        articleInfoVO.setIsLike(like);
        articleInfoVO.setActiveReadType(activeReadeType);
        return articleInfoVO;
    }


    public Page<ApiArticleSearchVO> searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new BadRequestException(PARAMS_ILLEGAL.getDesc());
        }
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        return searchStrategyContext.executeSearchStrategy(strategy, keywords);
    }


    public ListPublishedArticleResponse listPublished() {
        ListArticleRequest listArticleRequest = new ListArticleRequest();
        listArticleRequest.setIsPublish(1);
        List<ArticleView> articleViews = articleService.listArticleSummary(listArticleRequest);

        Map<String, List<ArticleView>> monthlyArticleMap = articleViews.stream().collect(Collectors.groupingBy(a -> DateHelper.toYearAndMonth(a.getCreateTime())));
        long total = monthlyArticleMap.values().stream().mapToLong(Collection::size).sum();
        List<ListPublishedArticleResponse.MonthlyArticleView> monthlyArticleViews = monthlyArticleMap.entrySet().stream().map(entry -> {
            List<PublishedArticleView> publishedArticleViews = entry.getValue().stream().map(article -> {
                PublishedArticleView publishedArticleView = new PublishedArticleView();
                publishedArticleView.setId(article.getId());
                publishedArticleView.setUserId(article.getUserId());
                publishedArticleView.setCategoryId(article.getCategoryId());
                publishedArticleView.setTitle(article.getTitle());
                publishedArticleView.setAvatar(article.getAvatar());
                publishedArticleView.setSummary(article.getSummary());
                publishedArticleView.setReadType(article.getReadType());
                publishedArticleView.setIsStick(article.getIsStick());
                publishedArticleView.setIsOriginal(article.getIsOriginal());
                publishedArticleView.setOriginalUrl(article.getOriginalUrl());
                publishedArticleView.setKeywords(article.getKeywords());
                publishedArticleView.setAddress(article.getAddress());
                publishedArticleView.setQuantity(article.getQuantity());
                publishedArticleView.setIsCarousel(article.getIsCarousel());
                publishedArticleView.setIsRecommend(article.getIsRecommend());
                publishedArticleView.setCreateTime(article.getCreateTime());
                publishedArticleView.setCreateMonthlyDate(article.getCreateTime());
                publishedArticleView.setUpdateTime(article.getUpdateTime());
                return publishedArticleView;
            }).toList();
            ListPublishedArticleResponse.MonthlyArticleView monthlyArticleView = new ListPublishedArticleResponse.MonthlyArticleView();
            monthlyArticleView.setMonth(entry.getKey());
            monthlyArticleView.setArticles(publishedArticleViews);
            return monthlyArticleView;
        }).toList();

        ListPublishedArticleResponse response = new ListPublishedArticleResponse();
        response.setTotal(total);
        response.setMonthlyArticles(monthlyArticleViews);
        return response;
    }


    public void articleLike(String articleId) {
        String userId = StpUtil.getLoginIdAsString();
        String articleLikeKey = ARTICLE_USER_LIKE + userId;
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            redisService.sRemove(articleLikeKey, articleId);
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId, 1L);
        } else {
            redisService.sAdd(articleLikeKey, articleId);
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId, 1L);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertArticle(String userId, String ipAddress, APICreateArticleRequest request) {
        CreateArticleRequest createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setTitle(request.getTitle());
        createArticleRequest.setAvatar(request.getAvatar());
        createArticleRequest.setSummary(request.getSummary());
        createArticleRequest.setQuantity(0);
        createArticleRequest.setContent(request.getContent());
        createArticleRequest.setContentMd(request.getContentMd());
        createArticleRequest.setKeywords(request.getKeywords());
        createArticleRequest.setReadType(0);
        createArticleRequest.setIsStick(0);
        createArticleRequest.setIsOriginal(request.getIsOriginal());
        createArticleRequest.setOriginalUrl(request.getOriginalUrl());
        createArticleRequest.setCategoryId(request.getCategoryId());
        createArticleRequest.setIsPublish(request.getIsPublish());
        createArticleRequest.setIsCarousel(0);
        createArticleRequest.setIsRecommend(0);
        createArticleRequest.setTagIds(request.getTagList());
        articleService.addArticle(userId, ipAddress, createArticleRequest);
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateMyArticle(String userId, String ipAddress, APIUpdateArticleRequest request) {
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();
        updateArticleRequest.setTitle(request.getTitle());
        updateArticleRequest.setAvatar(request.getAvatar());
        updateArticleRequest.setSummary(request.getSummary());
        updateArticleRequest.setQuantity(0);
        updateArticleRequest.setContent(request.getContent());
        updateArticleRequest.setContentMd(request.getContentMd());
        updateArticleRequest.setKeywords(request.getKeywords());
        updateArticleRequest.setReadType(0);
        updateArticleRequest.setIsStick(0);
        updateArticleRequest.setIsOriginal(request.getIsOriginal());
        updateArticleRequest.setOriginalUrl(request.getOriginalUrl());
        updateArticleRequest.setCategoryId(request.getCategoryId());
        updateArticleRequest.setIsPublish(request.getIsPublish());
        updateArticleRequest.setIsCarousel(0);
        updateArticleRequest.setIsRecommend(0);
        updateArticleRequest.setTagIds(request.getTagList());
        articleService.updateArticle(userId, request.getId(), updateArticleRequest);
    }


    public Page<RecommendedArticleVO> listByUserId(String userId, Integer type) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        Page<Object> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        ListArticleRequest listArticleRequest = new ListArticleRequest();
        listArticleRequest.setUserId(userId);
        listArticleRequest.setReadType(type);
        return searchRecommendedArticlePage(userId, page, listArticleRequest);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteMyArticle(String id) {
        Article article = articleMapper.selectById(id);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only delete your own article!");
        }
        articleMapper.deleteById(id);
        articleTagService.deleteByArticleIds(Set.of(id));
    }


    public ArticlePostDTO getById(String id) {
        Article article = articleService.getById(id);
        if (Objects.isNull(article)) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only read your own article detail!");
        }
        Map<String, List<Tag>> articleTagListMap = getArticleTagListMap(Set.of(id));
        List<String> tagIds = articleTagListMap.get(article.getId()).stream().map(Tag::getId).toList();

        ArticlePostDTO articlePostDTO = new ArticlePostDTO();
        articlePostDTO.setId(article.getId());
        articlePostDTO.setTitle(article.getTitle());
        articlePostDTO.setSummary(article.getSummary());
        articlePostDTO.setAvatar(article.getAvatar());
        articlePostDTO.setCategoryId(article.getCategoryId());
        articlePostDTO.setIsPublish(article.getIsPublish());
        articlePostDTO.setIsOriginal(article.getIsOriginal());
        articlePostDTO.setOriginalUrl(article.getOriginalUrl());
        articlePostDTO.setContent(article.getContent());
        articlePostDTO.setContentMd(article.getContentMd());
        articlePostDTO.setKeywords(article.getKeywords());
        articlePostDTO.setUserId(article.getUserId());
        articlePostDTO.setTagList(tagIds);
        return articlePostDTO;
    }


    public void checkCode(String code) {
        String key = RedisConstants.WECHAT_CODE + code;
        Object redisCode = redisService.getCacheObject(key);
        if (ObjectUtil.isNull(redisCode)) {
            throw new BadRequestException(ERROR_EXCEPTION_MOBILE_CODE.getDesc());
        }

        List<Object> cacheList = redisService.getCacheList(CHECK_CODE_IP);
        if (cacheList.isEmpty()) {
            cacheList = new ArrayList<>();
        }
        cacheList.add(IpUtil.getIp());
        redisService.setCacheList(CHECK_CODE_IP, cacheList);
        redisService.deleteObject(key);
    }
}
