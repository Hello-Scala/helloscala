package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.RedisConstants;
import com.helloscala.common.dto.article.ArticlePostDTO;
import com.helloscala.common.entity.*;
import com.helloscala.common.enums.ReadTypeEnum;
import com.helloscala.common.enums.SearchModelEnum;
import com.helloscala.common.mapper.*;
import com.helloscala.common.service.*;
import com.helloscala.common.strategy.context.SearchStrategyContext;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import com.helloscala.common.vo.article.ArticleInfoVO;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.service.ApiArticleService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.helloscala.common.RedisConstants.*;
import static com.helloscala.common.ResultCode.ERROR_EXCEPTION_MOBILE_CODE;
import static com.helloscala.common.ResultCode.PARAMS_ILLEGAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiArticleServiceImpl implements ApiArticleService {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final RedisService redisService;
    private final TagService tagService;
    private final CommentService commentService;
    private final CollectMapper collectMapper;
    private final FollowedMapper followedMapper;
    private final ArticleTagService articleTagService;
    private final SystemConfigService systemConfigService;
    private final SearchStrategyContext searchStrategyContext;

    @Override
    public Page<RecommendedArticleVO> selectArticleList(String categoryId, String tagId, String orderByDescColumn) {
        Page<RecommendedArticleVO> articlePage = articleMapper.selectPublicArticleList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                categoryId, tagId, orderByDescColumn);
        List<RecommendedArticleVO> records = articlePage.getRecords();

        Set<String> articleIdSet = records.stream().map(RecommendedArticleVO::getId).collect(Collectors.toSet());

        Map<String, List<Tag>> articleTagListMap = getArticleTagListMap(articleIdSet);

        List<Comment> comments = commentService.listArticleComment(articleIdSet);
        Map<String, List<Comment>> commentMap = comments.stream().collect(Collectors.groupingBy(Comment::getArticleId));

        Map<String, Object> articleLikeCountMap = redisService.getCacheMap(ARTICLE_LIKE_COUNT);

        records.forEach(item -> {
            List<Comment> articleComments = commentMap.getOrDefault(item.getId(), new ArrayList<>());
            List<Tag> tagList = articleTagListMap.getOrDefault(item.getId(), new ArrayList<>());
            if (articleLikeCountMap != null && !articleLikeCountMap.isEmpty()) {
                Object obj = articleLikeCountMap.get(item.getId().toString());
                item.setLikeCount(obj == null ? 0 : obj);
            }
            item.setFormatCreateTime(RelativeDateFormat.format(item.getCreateTime()));
            item.setCommentCount(articleComments.size());
            item.setTagList(tagList);
        });
        return articlePage;
    }

    @NotNull
    @Override
    public Map<String, List<Tag>> getArticleTagListMap(Set<String> articleIdSet) {
        if (ObjectUtil.isEmpty(articleIdSet)) {
            return new HashMap<>();
        }
        List<ArticleTag> articleTags = articleTagService.listByArticleIds(articleIdSet);
        Map<String, List<ArticleTag>> articleTagMap = articleTags.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        Set<String> tagIdSet = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toSet());
        List<Tag> tags = tagService.listByIds(tagIdSet);
        Map<String, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, Function.identity()));
        return articleTagMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(at -> tagMap.get(at.getTagId())).filter(Objects::nonNull).toList()));
    }

    @Override
    public ArticleInfoVO selectArticleInfo(String id) {
        ArticleInfoVO articleInfoVO = articleMapper.selectArticleByIdToVO(id);
        if (articleInfoVO == null) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        Long collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, id));
        Map<String, List<Tag>> articleTagListMap = getArticleTagListMap(Set.of(articleInfoVO.getId()));
        List<Tag> tags = articleTagListMap.get(articleInfoVO.getId());

        Long commentCount = commentService.countByArticleId(id);

        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);

        articleInfoVO.setTagList(tags);
        articleInfoVO.setCollectCount(collectCount.intValue());
        articleInfoVO.setCommentCount(commentCount);
        if (map != null && !map.isEmpty()) {
            articleInfoVO.setLikeCount(map.get(id.toString()));
        }
        String userId = (String) StpUtil.getLoginIdDefaultNull();
        if (userId != null) {
            String articleLikeKey = ARTICLE_USER_LIKE + userId;
            if (redisService.sIsMember(articleLikeKey, id)) {
                articleInfoVO.setIsLike(true);
                if (articleInfoVO.getReadType() == ReadTypeEnum.LIKE.index) {
                    articleInfoVO.setActiveReadType(true);
                }
            }
            if (articleInfoVO.getReadType() == ReadTypeEnum.COMMENT.index) {
                Long count = commentService.countByUserId(userId);
                if (Objects.nonNull(count) && count > 0) {
                    articleInfoVO.setActiveReadType(true);
                }
            }

            Long collect = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, userId).eq(Collect::getArticleId, id));
            articleInfoVO.setIsCollect(collect.intValue());

            Long followed = followedMapper.selectCount(new LambdaQueryWrapper<Followed>().eq(Followed::getUserId, userId)
                    .eq(Followed::getFollowedUserId, articleInfoVO.getUserId()));
            articleInfoVO.setIsFollowed(followed.intValue());
        }

        if (articleInfoVO.getReadType() == ReadTypeEnum.CODE.index) {
            List<Object> cacheList = redisService.getCacheList(RedisConstants.CHECK_CODE_IP);
            String ip = IpUtil.getIp();
            if (cacheList.contains(ip)) {
                articleInfoVO.setActiveReadType(true);
            }
        }

        redisService.incrArticle(id, ARTICLE_READING, IpUtil.getIp());
        return articleInfoVO;
    }

    @Override
    public Page<ApiArticleSearchVO> searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new BadRequestException(PARAMS_ILLEGAL.getDesc());
        }
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        return searchStrategyContext.executeSearchStrategy(strategy, keywords);
    }

    @Override
    public List<Article> listPublished() {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Article::getIsPublish, 1);
        articleQuery.orderByDesc(Article::getCreateTime);
        return articleMapper.selectList(articleQuery);
    }

    @Override
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertArticle(ArticlePostDTO dto) {
        Article article = BeanCopyUtil.copyObject(dto, Article.class);
        article.setUserId(StpUtil.getLoginIdAsString());
        int insert = articleMapper.insert(article);
        if (insert > 0) {
            articleTagService.insertIgnoreArticleTags(article.getId(), new HashSet<>(dto.getTagList()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMyArticle(ArticlePostDTO dto) {
        Article article = BeanCopyUtil.copyObject(dto, Article.class);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only modify your own article!");
        }
        articleMapper.updateById(article);
        articleTagService.resetArticleTags(article.getId(), new HashSet<>(dto.getTagList()));
    }

    @Override
    public Page<RecommendedArticleVO> listByUserId(String userId, Integer type) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        Page<Object> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<RecommendedArticleVO> articlePage = articleMapper.selectMyArticle(page, userId, type);

        List<RecommendedArticleVO> records = articlePage.getRecords();
        Set<String> articleIdSet = records.stream().map(RecommendedArticleVO::getId).collect(Collectors.toSet());
        Map<String, List<Tag>> articleTagListMap = getArticleTagListMap(articleIdSet);
        records.forEach(item -> {
            item.setTagList(articleTagListMap.get(item.getId()));
            item.setFormatCreateTime(RelativeDateFormat.format(item.getCreateTime()));
        });
        return articlePage;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMyArticle(String id) {
        Article article = articleMapper.selectById(id);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only delete your own article!");
        }
        articleMapper.deleteById(id);
        articleTagService.deleteByArticleIds(Set.of(id));
    }

    @Override
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

    @Override
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
