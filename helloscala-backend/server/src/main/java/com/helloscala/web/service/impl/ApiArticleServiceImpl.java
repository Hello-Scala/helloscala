package com.helloscala.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.RedisConstants;
import com.helloscala.common.ResponseResult;
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
import com.helloscala.common.vo.article.ApiArchiveVO;
import com.helloscala.common.vo.article.ApiArticleInfoVO;
import com.helloscala.common.vo.article.ApiArticleListVO;
import com.helloscala.common.vo.article.ApiArticleSearchVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.service.ApiArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.helloscala.common.RedisConstants.*;
import static com.helloscala.common.ResultCode.ERROR_EXCEPTION_MOBILE_CODE;
import static com.helloscala.common.ResultCode.PARAMS_ILLEGAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiArticleServiceImpl implements ApiArticleService {

    private final ArticleMapper articleMapper;

    private final RedisService redisService;

    private final TagsMapper tagsMapper;

    private final CommentMapper commentMapper;

    private final SystemConfigService systemConfigService;

    private final CollectMapper collectMapper;
    private final FollowedMapper followedMapper;

    private final SearchStrategyContext searchStrategyContext;

    @Override
    public ResponseResult selectArticleList(Integer categoryId, Integer tagId, String orderByDescColumn) {
        Page<ApiArticleListVO> articlePage = articleMapper.selectPublicArticleList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                categoryId, tagId, orderByDescColumn);
        articlePage.getRecords().forEach(item -> {
            setCommentAndLike(item);
//            int collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, item.getId()));
//            item.setCollectCount(collectCount);
//            if (StpUtil.getLoginIdDefaultNull() != null) {
//                collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, item.getId())
//                        .eq(Collect::getUserId,StpUtil.getLoginIdAsString()));
//                item.setIsCollect(collectCount > 0);
//            }
            item.setFormatCreateTime(RelativeDateFormat.format(item.getCreateTime()));
        });
        return ResponseResult.success(articlePage);
    }

    @Override
    public ResponseResult selectArticleInfo(Integer id) {
        ApiArticleInfoVO apiArticleInfoVO = articleMapper.selectArticleByIdToVO(id);
        if (apiArticleInfoVO == null) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        Long collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, id));
        apiArticleInfoVO.setCollectCount(collectCount.intValue());
        List<Tag> list = tagsMapper.selectTagByArticleId(apiArticleInfoVO.getId());
        apiArticleInfoVO.setTagList(list);
        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, id));
        apiArticleInfoVO.setCommentCount(comments.size());
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);
        if (map != null && !map.isEmpty()) {
            apiArticleInfoVO.setLikeCount(map.get(id.toString()));
        }
        Object userId = StpUtil.getLoginIdDefaultNull();
        if (userId != null) {
            String articleLikeKey = ARTICLE_USER_LIKE + userId;
            if (redisService.sIsMember(articleLikeKey, id)) {
                apiArticleInfoVO.setIsLike(true);
                if (apiArticleInfoVO.getReadType() == ReadTypeEnum.LIKE.index) {
                    apiArticleInfoVO.setActiveReadType(true);
                }
            }
            if (apiArticleInfoVO.getReadType() == ReadTypeEnum.COMMENT.index) {
                Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getUserId, userId));
                if (count != null && count > 0) {
                    apiArticleInfoVO.setActiveReadType(true);
                }
            }

            Long collect = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, userId).eq(Collect::getArticleId, id));
            apiArticleInfoVO.setIsCollect(collect.intValue());

            Long followed = followedMapper.selectCount(new LambdaQueryWrapper<Followed>().eq(Followed::getUserId, userId)
                    .eq(Followed::getFollowedUserId, apiArticleInfoVO.getUserId()));
            apiArticleInfoVO.setIsFollowed(followed.intValue());
        }

        if (apiArticleInfoVO.getReadType() == ReadTypeEnum.CODE.index) {
            List<Object> cacheList = redisService.getCacheList(RedisConstants.CHECK_CODE_IP);
            String ip = IpUtil.getIp();
            if (cacheList.contains(ip)) {
                apiArticleInfoVO.setActiveReadType(true);
            }
        }

        redisService.incrArticle(id.longValue(), ARTICLE_READING, IpUtil.getIp());
        return ResponseResult.success(apiArticleInfoVO);
    }

    @Override
    public ResponseResult searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new BadRequestException(PARAMS_ILLEGAL.getDesc());
        }
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        Page<ApiArticleSearchVO> page = searchStrategyContext.executeSearchStrategy(strategy, keywords);

        return ResponseResult.success(page);
    }

    @Override
    public ResponseResult archive() {
        List<ApiArchiveVO> articleList = articleMapper.selectListArchive();
        Map<String, List<ApiArchiveVO>> resultList = articleList.stream().collect(Collectors.groupingBy(ApiArchiveVO::getTime));
        Object[] keyArr = resultList.keySet().toArray();
        Arrays.sort(keyArr);
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = keyArr.length - 1; i >= 0; i--) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", keyArr[i]);
            List<ApiArchiveVO> list = resultList.get(keyArr[i]);
            Collections.sort(list, (o1, o2) -> o2.getFormatTime().compareTo(o1.getFormatTime()));
            map.put("list", list);
            result.add(map);
        }
        return ResponseResult.success(result).putExtra("total", articleList.size());
    }

    @Override
    public ResponseResult articleLike(Integer articleId) {
        String userId = StpUtil.getLoginIdAsString();
        String articleLikeKey = ARTICLE_USER_LIKE + userId;
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            redisService.sRemove(articleLikeKey, articleId);
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            redisService.sAdd(articleLikeKey, articleId);
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }

        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertArticle(ArticlePostDTO dto) {
        Article article = BeanCopyUtil.copyObject(dto, Article.class);
        article.setUserId(StpUtil.getLoginIdAsString());
        int insert = articleMapper.insert(article);
        if (insert > 0) {
            tagsMapper.saveArticleTags(article.getId(), dto.getTagList());
        }
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateMyArticle(ArticlePostDTO dto) {
        Article article = BeanCopyUtil.copyObject(dto, Article.class);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only modify your own article!");
        }
        articleMapper.updateById(article);
        tagsMapper.deleteByArticleIds(Collections.singletonList(article.getId()));
        tagsMapper.saveArticleTags(article.getId(), dto.getTagList());
        return ResponseResult.success();
    }

    @Override
    public ResponseResult readMarkdownFile(MultipartFile file) {
        String fileName = file.getOriginalFilename().split(".md")[0];
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = file.getInputStream();
            byte[] buffer = new byte[1024];

            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, length));
            }

            inputStream.close();

        } catch (IOException e) {
            log.error("Failed to read markdown file!", e);
            throw new BadRequestException("Markdown file read failed!");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("content", sb.toString());
        map.put("fileName", fileName);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult selectArticleByUserId(String userId, Integer type) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        Page<ApiArticleListVO> list = articleMapper.selectMyArticle(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), userId, type);
        list.getRecords().forEach(item -> {
            List<Tag> tags = tagsMapper.selectTagByArticleId(item.getId());
            item.setTagList(tags);

            item.setFormatCreateTime(RelativeDateFormat.format(item.getCreateTime()));
        });
        return ResponseResult.success(list);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMyArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only delete your own article!");
        }
        articleMapper.deleteById(id);
        tagsMapper.deleteByArticleIds(Collections.singletonList(id));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult selectMyArticleInfo(Long id) {
        ArticlePostDTO articlePostDTO = articleMapper.selectMyArticleInfo(id);
        if (!articlePostDTO.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only read your own article detail!");
        }
        List<Tag> tags = tagsMapper.selectTagByArticleId(id);
        List<Long> tagList = tags.stream().map(Tag::getId).collect(Collectors.toList());
        articlePostDTO.setTagList(tagList);
        return ResponseResult.success(articlePostDTO);
    }

    @Override
    public ResponseResult checkCode(String code) {
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
        return ResponseResult.success("Verified!");
    }


    private void setCommentAndLike(ApiArticleListVO item) {
        List<Tag> list = tagsMapper.selectTagByArticleId(item.getId());
        Long commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getArticleId, item.getId()));
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);
        if (map != null && !map.isEmpty()) {
            Object obj = map.get(item.getId().toString());
            item.setLikeCount(obj == null ? 0 : obj);
        }
        item.setTagList(list);
        item.setCommentCount(commentCount.intValue());
    }
}
