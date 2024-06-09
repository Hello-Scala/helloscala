package com.helloscala.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.RedisConstants;
import com.helloscala.common.ResponseResult;
import com.helloscala.dto.article.ArticlePostDTO;
import com.helloscala.entity.Article;
import com.helloscala.entity.Collect;
import com.helloscala.entity.Comment;
import com.helloscala.entity.Followed;
import com.helloscala.entity.SystemConfig;
import com.helloscala.entity.Tags;
import com.helloscala.enums.ReadTypeEnum;
import com.helloscala.enums.SearchModelEnum;
import com.helloscala.exception.BusinessException;
import com.helloscala.handle.RelativeDateFormat;
import com.helloscala.mapper.ArticleMapper;
import com.helloscala.mapper.CollectMapper;
import com.helloscala.mapper.CommentMapper;
import com.helloscala.mapper.FollowedMapper;
import com.helloscala.mapper.TagsMapper;
import com.helloscala.service.ApiArticleService;
import com.helloscala.service.RedisService;
import com.helloscala.service.SystemConfigService;
import com.helloscala.strategy.context.SearchStrategyContext;
import com.helloscala.utils.BeanCopyUtil;
import com.helloscala.utils.IpUtil;
import com.helloscala.utils.PageUtil;
import com.helloscala.vo.article.ApiArchiveVO;
import com.helloscala.vo.article.ApiArticleInfoVO;
import com.helloscala.vo.article.ApiArticleListVO;
import com.helloscala.vo.article.ApiArticleSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.helloscala.common.RedisConstants.ARTICLE_LIKE_COUNT;
import static com.helloscala.common.RedisConstants.ARTICLE_READING;
import static com.helloscala.common.RedisConstants.ARTICLE_USER_LIKE;
import static com.helloscala.common.RedisConstants.CHECK_CODE_IP;
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

    /**
     * 获取文章列表
     *
     * @return
     */
    @Override
    public ResponseResult selectArticleList(Integer categoryId, Integer tagId, String orderByDescColumn) {
        Page<ApiArticleListVO> articlePage = articleMapper.selectPublicArticleList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                categoryId, tagId, orderByDescColumn);
        articlePage.getRecords().forEach(item -> {
            setCommentAndLike(item);
//            //获取文章
//            int collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, item.getId()));
//            item.setCollectCount(collectCount);
//            //判断当前登录用户是否收藏该文章 标记为收藏
//            if (StpUtil.getLoginIdDefaultNull() != null) {
//                collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, item.getId())
//                        .eq(Collect::getUserId,StpUtil.getLoginIdAsString()));
//                item.setIsCollect(collectCount > 0);
//            }
            //格式化时间为几秒前 几分钟前等
            item.setFormatCreateTime(RelativeDateFormat.format(item.getCreateTime()));
        });
        return ResponseResult.success(articlePage);
    }

    /**
     * 获取文章详情
     *
     * @return
     */
    @Override
    public ResponseResult selectArticleInfo(Integer id) {
        ApiArticleInfoVO apiArticleInfoVO = articleMapper.selectArticleByIdToVO(id);
        if (apiArticleInfoVO == null) {
            throw new BusinessException("Article not found, id={}!", id);
        }
        //获取收藏量
        Long collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getArticleId, id));
        apiArticleInfoVO.setCollectCount(collectCount.intValue());
        //获取标签
        List<Tags> list = tagsMapper.selectTagByArticleId(apiArticleInfoVO.getId());
        apiArticleInfoVO.setTagList(list);
        //获取评论数量
        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, id));
        apiArticleInfoVO.setCommentCount(comments.size());
        //获取点赞数量
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);
        if (map != null && !map.isEmpty()) {
            apiArticleInfoVO.setLikeCount(map.get(id.toString()));
        }
        //获取当前登录用户是否点赞该文章
        Object userId = StpUtil.getLoginIdDefaultNull();
        if (userId != null) {
            String articleLikeKey = ARTICLE_USER_LIKE + userId;
            if (redisService.sIsMember(articleLikeKey, id)) {
                apiArticleInfoVO.setIsLike(true);
                //校验文章用户是否已经点赞过
                if (apiArticleInfoVO.getReadType() == ReadTypeEnum.LIKE.index) {
                    apiArticleInfoVO.setActiveReadType(true);
                }
            }
            //校验文章用户是否已经评论过
            if (apiArticleInfoVO.getReadType() == ReadTypeEnum.COMMENT.index) {
                Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getUserId, userId));
                if (count != null && count > 0) {
                    apiArticleInfoVO.setActiveReadType(true);
                }
            }

            //校验用户是否收藏文章
            Long collect = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, userId).eq(Collect::getArticleId, id));
            apiArticleInfoVO.setIsCollect(collect.intValue());

            //校验用户是否关注该文章作者
            Long followed = followedMapper.selectCount(new LambdaQueryWrapper<Followed>().eq(Followed::getUserId, userId)
                    .eq(Followed::getFollowedUserId, apiArticleInfoVO.getUserId()));
            apiArticleInfoVO.setIsFollowed(followed.intValue());
        }

        //校验文章是否已经进行过扫码验证
        if (apiArticleInfoVO.getReadType() == ReadTypeEnum.CODE.index) {
            List<Object> cacheList = redisService.getCacheList(RedisConstants.CHECK_CODE_IP);
            String ip = IpUtil.getIp();
            if (cacheList.contains(ip)) {
                apiArticleInfoVO.setActiveReadType(true);
            }
        }

        //增加文章阅读量
        redisService.incrArticle(id.longValue(), ARTICLE_READING, IpUtil.getIp());
        return ResponseResult.success(apiArticleInfoVO);
    }

    /**
     * 搜索文章
     *
     * @return
     */
    @Override
    public ResponseResult searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new BusinessException(PARAMS_ILLEGAL.getDesc());
        }
        //获取搜索模式（es搜索或mysql搜索）
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        //搜索逻辑
        Page<ApiArticleSearchVO> page = searchStrategyContext.executeSearchStrategy(strategy, keywords);

        return ResponseResult.success(page);
    }

    /**
     * 获取归档
     *
     * @return
     */
    @Override
    public ResponseResult archive() {
        List<ApiArchiveVO> articleList = articleMapper.selectListArchive();
        //按日期分组
        Map<String, List<ApiArchiveVO>> resultList = articleList.stream().collect(Collectors.groupingBy(ApiArchiveVO::getTime));
        Object[] keyArr = resultList.keySet().toArray();  //获取resultList的所有key值数组
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

    /**
     * 文章点赞
     *
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult articleLike(Integer articleId) {
        String userId = StpUtil.getLoginIdAsString();
        // 判断是否点赞
        String articleLikeKey = ARTICLE_USER_LIKE + userId;
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisService.sRemove(articleLikeKey, articleId);
            // 文章点赞量-1
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 未点赞则增加文章id
            redisService.sAdd(articleLikeKey, articleId);
            // 文章点赞量+1
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }

        return ResponseResult.success();
    }

    /**
     * 用户添加文章
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertArticle(ArticlePostDTO dto) {
        Article article = BeanCopyUtil.copyObject(dto, Article.class);
        article.setUserId(StpUtil.getLoginIdAsString());
        int insert = articleMapper.insert(article);
        //添加标签
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
            throw new BusinessException("Can only modify your own article!");
        }
        articleMapper.updateById(article);

        //先删出所有标签
        tagsMapper.deleteByArticleIds(Collections.singletonList(article.getId()));
        //然后新增标签
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
            throw new BusinessException("Markdown file read failed!");
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
            List<Tags> tags = tagsMapper.selectTagByArticleId(item.getId());
            item.setTagList(tags);

            item.setFormatCreateTime(RelativeDateFormat.format(item.getCreateTime()));
        });
        return ResponseResult.success(list);

    }

    /**
     * 删除我的文章
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMyArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BusinessException("Can only delete your own article!");
        }
        articleMapper.deleteById(id);
        tagsMapper.deleteByArticleIds(Collections.singletonList(id));
        return ResponseResult.success();
    }

    /**
     * 获取我的文章详情
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult selectMyArticleInfo(Long id) {
        ArticlePostDTO articlePostDTO = articleMapper.selectMyArticleInfo(id);
        if (!articlePostDTO.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BusinessException("Can only read your own article detail!");
        }
        List<Tags> tags = tagsMapper.selectTagByArticleId(id);
        List<Long> tagList = tags.stream().map(Tags::getId).collect(Collectors.toList());
        articlePostDTO.setTagList(tagList);
        return ResponseResult.success(articlePostDTO);
    }

    /**
     * 校验文章验证码(验证码通过关注公众号获取)
     *
     * @return
     */
    @Override
    public ResponseResult checkCode(String code) {
        //校验验证码
        String key = RedisConstants.WECHAT_CODE + code;
        Object redisCode = redisService.getCacheObject(key);
        if (ObjectUtil.isNull(redisCode)) {
            throw new BusinessException(ERROR_EXCEPTION_MOBILE_CODE.getDesc());
        }

        //将ip存在redis 有效期一天，当天无需再进行验证码校验
        List<Object> cacheList = redisService.getCacheList(CHECK_CODE_IP);
        if (cacheList.isEmpty()) {
            cacheList = new ArrayList<>();
        }
        cacheList.add(IpUtil.getIp());
        redisService.setCacheList(CHECK_CODE_IP, cacheList);
        //通过删除验证码
        redisService.deleteObject(key);
        return ResponseResult.success("Verified!");
    }


    /**
     * 设置评论量和点赞量
     *
     * @param item
     */
    private void setCommentAndLike(ApiArticleListVO item) {
        List<Tags> list = tagsMapper.selectTagByArticleId(item.getId());
        Long commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getArticleId, item.getId()));
        //获取点赞数量
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);
        if (map != null && !map.isEmpty()) {
            Object obj = map.get(item.getId().toString());
            item.setLikeCount(obj == null ? 0 : obj);
        }
        item.setTagList(list);
        item.setCommentCount(commentCount.intValue());
    }
}
