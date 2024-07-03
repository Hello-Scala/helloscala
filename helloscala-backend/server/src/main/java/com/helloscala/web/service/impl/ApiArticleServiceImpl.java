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
import com.helloscala.common.service.RedisService;
import com.helloscala.common.service.SystemConfigService;
import com.helloscala.common.strategy.context.SearchStrategyContext;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import com.helloscala.common.vo.article.ArticleInfoVO;
import com.helloscala.common.vo.article.ListArticleVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.service.ApiArticleService;
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

    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;

    private final RedisService redisService;

    private final TagsMapper tagMapper;

    private final CommentMapper commentMapper;

    private final SystemConfigService systemConfigService;

    private final CollectMapper collectMapper;
    private final FollowedMapper followedMapper;

    private final SearchStrategyContext searchStrategyContext;

    @Override
    public Page<ListArticleVO> selectArticleList(Integer categoryId, Integer tagId, String orderByDescColumn) {
        Page<ListArticleVO> articlePage = articleMapper.selectPublicArticleList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                categoryId, tagId, orderByDescColumn);
        List<ListArticleVO> records = articlePage.getRecords();

        Set<Long> articleIdSet = records.stream().map(ListArticleVO::getId).collect(Collectors.toSet());

        Map<Long, List<Tag>> articleTagListMap = getArticleTagListMap(articleIdSet);

        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getArticleId)
                .in(Comment::getArticleId, articleIdSet);
        List<Comment> comments = commentMapper.selectList(commentQuery);
        Map<Long, List<Comment>> commentMap = comments.stream().collect(Collectors.groupingBy(Comment::getArticleId));

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
    public Map<Long, List<Tag>> getArticleTagListMap(Set<Long> articleIdSet) {
        LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
        articleTagQuery.in(ArticleTag::getArticleId, articleIdSet);
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQuery);

        Map<Long, List<ArticleTag>> articleTagMap = articleTags.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        Set<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toSet());
        LambdaQueryWrapper<Tag> tagQuery = new LambdaQueryWrapper<>();
        tagQuery.in(Tag::getId, tagIds);
        List<Tag> tags = tagMapper.selectList(tagQuery);
        Map<Long, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, Function.identity()));
        return articleTagMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(at -> tagMap.get(at.getTagId())).filter(Objects::nonNull).toList()));
    }

    @Override
    public ArticleInfoVO selectArticleInfo(Integer id) {
        ArticleInfoVO articleInfoVO = articleMapper.selectArticleByIdToVO(id);
        if (articleInfoVO == null) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        Long collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, id));
        Map<Long, List<Tag>> articleTagListMap = getArticleTagListMap(Set.of(articleInfoVO.getId()));
        List<Tag> tags = articleTagListMap.get(articleInfoVO.getId());

        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, id));

        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);

        articleInfoVO.setTagList(tags);
        articleInfoVO.setCollectCount(collectCount.intValue());
        articleInfoVO.setCommentCount(comments.size());
        if (map != null && !map.isEmpty()) {
            articleInfoVO.setLikeCount(map.get(id.toString()));
        }
        Object userId = StpUtil.getLoginIdDefaultNull();
        if (userId != null) {
            String articleLikeKey = ARTICLE_USER_LIKE + userId;
            if (redisService.sIsMember(articleLikeKey, id)) {
                articleInfoVO.setIsLike(true);
                if (articleInfoVO.getReadType() == ReadTypeEnum.LIKE.index) {
                    articleInfoVO.setActiveReadType(true);
                }
            }
            if (articleInfoVO.getReadType() == ReadTypeEnum.COMMENT.index) {
                Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getUserId, userId));
                if (count != null && count > 0) {
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

        redisService.incrArticle(id.longValue(), ARTICLE_READING, IpUtil.getIp());
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
    public void articleLike(Integer articleId) {
        String userId = StpUtil.getLoginIdAsString();
        String articleLikeKey = ARTICLE_USER_LIKE + userId;
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            redisService.sRemove(articleLikeKey, articleId);
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            redisService.sAdd(articleLikeKey, articleId);
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertArticle(ArticlePostDTO dto) {
        Article article = BeanCopyUtil.copyObject(dto, Article.class);
        article.setUserId(StpUtil.getLoginIdAsString());
        int insert = articleMapper.insert(article);
        if (insert > 0) {
            tagMapper.saveArticleTags(article.getId(), dto.getTagList());
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
        tagMapper.deleteByArticleIds(Collections.singletonList(article.getId()));
        tagMapper.saveArticleTags(article.getId(), dto.getTagList());
    }

    @Override
    public Page<ListArticleVO> listByUserId(String userId, Integer type) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        Page<Object> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<ListArticleVO> list = articleMapper.selectMyArticle(page, userId, type);

        List<ListArticleVO> records = list.getRecords();
        Set<Long> articleIdSet = records.stream().map(ListArticleVO::getId).collect(Collectors.toSet());
        Map<Long, List<Tag>> articleTagListMap = getArticleTagListMap(articleIdSet);
        records.forEach(item -> {
            item.setTagList(articleTagListMap.get(item.getId()));
            item.setFormatCreateTime(RelativeDateFormat.format(item.getCreateTime()));
        });
        return list;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMyArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only delete your own article!");
        }
        articleMapper.deleteById(id);
        tagMapper.deleteByArticleIds(Collections.singletonList(id));
    }

    @Override
    public ArticlePostDTO selectMyArticleInfo(Long id) {
        ArticlePostDTO articlePostDTO = articleMapper.selectMyArticleInfo(id);
        if (Objects.isNull(articlePostDTO)) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        if (!articlePostDTO.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only read your own article detail!");
        }
        Map<Long, List<Tag>> articleTagListMap = getArticleTagListMap(Set.of(id));
        List<Long> tagIds = articleTagListMap.get(articlePostDTO.getId()).stream().map(Tag::getId).toList();
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
