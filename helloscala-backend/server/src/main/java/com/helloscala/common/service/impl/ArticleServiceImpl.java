package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResultCode;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.entity.*;
import com.helloscala.common.enums.DataEventEnum;
import com.helloscala.common.enums.YesOrNoEnum;
import com.helloscala.common.event.DataEventPublisherService;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.CategoryMapper;
import com.helloscala.common.mapper.TagMapper;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.service.*;
import com.helloscala.common.service.util.ArticleEntityHelper;
import com.helloscala.common.utils.*;
import com.helloscala.common.vo.article.ArticleVO;
import com.helloscala.common.web.exception.FailedDependencyException;
import com.helloscala.common.web.exception.ForbiddenException;
import com.helloscala.common.web.exception.NotFoundException;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final TagMapper tagMapper;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final RestTemplate restTemplate;
    private final ArticleTagService articleTagService;
    private final DataEventPublisherService dataEventPublisherService;
    @Value("${baidu.url}")
    private String baiduUrl;

    @Override
    public Page<ArticleVO> selectArticlePage(String title, Long tagId, Long categoryId, Integer isPublish) {
        List<String> articleIds = articleTagService.listArticleIds(tagId);

        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.in(ObjectUtil.isNotEmpty(articleIds), Article::getId, articleIds);
        articleQuery.like(StrUtil.isNotBlank(title), Article::getTitle, title);
        articleQuery.eq(Objects.nonNull(categoryId), Article::getCategoryId, categoryId);
        articleQuery.eq(Objects.nonNull(isPublish), Article::getIsPublish, isPublish);

        Page<Article> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<Article> articlePage = baseMapper.selectPage(page, articleQuery);

        List<Article> articles = articlePage.getRecords();
        Set<Long> articleIdSet = articles.stream().map(Article::getId).collect(Collectors.toSet());
        Map<Long, List<String>> articleTagNameMap = fetchArticleTagNameMap(articleIdSet);

        Set<String> categoryIds = articles.stream().map(a -> String.valueOf(a.getCategoryId())).collect(Collectors.toSet());
        List<Category> categories = categoryService.listByIds(categoryIds);
        Map<Long, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));

        Set<String> userIds = articles.stream().map(Article::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        List<ArticleVO> articleViews = articles.stream().map(item -> {
            User user = userMap.get(item.getUserId());
            Category category = categoryMap.get(item.getCategoryId());
            List<String> tagNames = articleTagNameMap.get(item.getId());

            ArticleVO articleVO = new ArticleVO();
            articleVO.setId(item.getId());
            articleVO.setUserId(item.getUserId());
            articleVO.setTitle(item.getTitle());
            if (Objects.nonNull(user)) {
                articleVO.setNickname(user.getNickname());
            }
            articleVO.setAvatar(item.getAvatar());
            articleVO.setReadType(item.getReadType());
            articleVO.setIsStick(item.getIsStick());
            articleVO.setIsOriginal(item.getIsOriginal());
            articleVO.setQuantity(item.getQuantity());
            articleVO.setCreateTime(item.getCreateTime());
            articleVO.setIsPublish(item.getIsPublish());
            if (Objects.nonNull(category)) {
                articleVO.setCategoryName(category.getName());
            }
            articleVO.setTagNames(String.join(",", tagNames));
            return articleVO;
        }).toList();

        Page<ArticleVO> resultPage = Page.of(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        resultPage.setRecords(articleViews);
        return resultPage;
    }

    @NotNull
    private Map<Long, List<String>> fetchArticleTagNameMap(Set<Long> articleIdSet) {
        List<ArticleTag> articleTags = articleTagService.listByArticleIds(articleIdSet);
        Map<Long, List<ArticleTag>> articleTagMap = articleTags.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        Set<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toSet());
        List<Tag> tags = tagService.listByIds(tagIds);
        Map<Long, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, Function.identity()));

        Map<Long, List<String>> articleTagNameMap = new HashMap<>();
        articleTagMap.forEach((articleId, tagList) -> {
            List<String> tagNameList = tagList.stream().map(articleTag -> Optional.ofNullable(tagMap.get(articleTag.getTagId())).map(Tag::getName).orElse(null))
                    .filter(Objects::nonNull).toList();
            articleTagNameMap.put(articleId, tagNameList);
        });
        return articleTagNameMap;
    }

    @Override
    public ArticleDTO selectArticleById(Long id) {
        Article article = getById(id);
        if (Objects.isNull(article)) {
            throw new NotFoundException("Article not found, id={}!", id);
        }
        Category category = categoryService.getById(article.getCategoryId());
        Map<Long, List<String>> articleTagNameMap = fetchArticleTagNameMap(Set.of(id));

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setUserId(article.getUserId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setAvatar(article.getAvatar());
        articleDTO.setSummary(article.getSummary());
        articleDTO.setQuantity(article.getQuantity());
        articleDTO.setContent(article.getContent());
        articleDTO.setContentMd(article.getContentMd());
        articleDTO.setKeywords(article.getKeywords());
        articleDTO.setReadType(article.getReadType());
        articleDTO.setIsStick(article.getIsStick());
        articleDTO.setIsOriginal(article.getIsOriginal());
        articleDTO.setOriginalUrl(article.getOriginalUrl());
        if (Objects.nonNull(category)) {
            articleDTO.setCategoryName(category.getName());
        }
        articleDTO.setIsPublish(article.getIsPublish());
        articleDTO.setIsCarousel(article.getIsCarousel());
        articleDTO.setIsRecommend(article.getIsRecommend());
        articleDTO.setTags(articleTagNameMap.getOrDefault(article.getId(), List.of()));
        articleDTO.setCreateTime(article.getCreateTime());
        articleDTO.setUpdateTime(article.getUpdateTime());
        return articleDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(String ipAddress, ArticleDTO article) {
        Long categoryId = getOrCreateCategory(article.getCategoryName());
        Set<String> tagNames = new HashSet<>(article.getTags());
        List<Tag> tags = tagService.listByNames(tagNames);
        Set<String> existTagNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        Set<String> tagNameToCreateList = tagNames.stream().filter(n -> !existTagNames.contains(n)).collect(Collectors.toSet());
        List<Tag> tagsCreated = tagService.bulkCreateByNames(tagNameToCreateList);
        List<Tag> articleTags = ListHelper.concat(tags, tagsCreated);

        Article blogArticle = BeanCopyUtil.copyObject(article, Article.class);
        blogArticle.setCategoryId(categoryId);
        if (StrUtil.isNotBlank(ipAddress)
                && !"UNKNOWN".equals(ipAddress)
                && ipAddress.split("\\|").length > 1) {
            String[] split = ipAddress.split("\\|");
            String address = split[1];
            blogArticle.setAddress(address);
        } else {
            blogArticle.setAddress("Earth");
        }
        int insert = baseMapper.insert(blogArticle);
        if (insert > 0) {
            Set<Long> tagIds = articleTags.stream().map(Tag::getId).collect(Collectors.toSet());
            articleTagService.resetArticleTags(article.getId(), tagIds);
        }

        //发布消息去同步es 不进行判断是否是发布状态了，因为后面修改成下架的话就还得去删除es里面的数据，多次一举了，在查询时添加条件发布状态为已发布
        dataEventPublisherService.publishData(DataEventEnum.ES_ADD_ARTICLE, ArticleEntityHelper.toElasticEntity(blogArticle));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(String userId, ArticleDTO articleDTO) {
        Long categoryId = getOrCreateCategory(articleDTO.getCategoryName());
        List<Long> tagList = getTagsList(articleDTO);

        Article article = BeanCopyUtil.copyObject(articleDTO, Article.class);
        article.setCategoryId(categoryId);
        baseMapper.updateById(article);
        articleTagService.resetArticleTags(article.getId(), new HashSet<>(tagList));
        dataEventPublisherService.publishData(DataEventEnum.ES_UPDATE_ARTICLE, ArticleEntityHelper.toElasticEntity(article));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchArticle(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        articleTagService.deleteByArticleIds(new HashSet<>(ids));
        dataEventPublisherService.publishData(DataEventEnum.ES_DELETE_ARTICLE, ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void topArticle(ArticleDTO article) {
        baseMapper.putTopArticle(article);
    }

    @Override
    public void seoArticle(List<Long> ids) {

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
    public void retch(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements title = document.getElementsByClass("title-article");
            Elements tags = document.getElementsByClass("tag-link");
            Elements content = document.getElementsByClass("article_content");
            if (StringUtils.isBlank(content.toString())) {
                throw new FailedDependencyException(ResultCode.FETCH_ARTICLE_FAILED.getDesc());
            }

            //爬取的是HTML内容，需要转成MD格式的内容
            String newContent = content.get(0).toString().replaceAll("<code>", "<code class=\"lang-java\">");
            MutableDataSet options = new MutableDataSet();
            String markdown = FlexmarkHtmlConverter.builder(options).build().convert(newContent)
                    .replace("lang-java", "java");

            Article entity = Article.builder().userId(StpUtil.getLoginIdAsString()).contentMd(markdown)
                    .categoryId(16L).isOriginal(YesOrNoEnum.NO.getCode()).originalUrl(url)
                    .title(title.get(0).text()).avatar("https://picsum.photos/500/300").content(newContent).build();

            baseMapper.insert(entity);
            List<Long> tagsId = new ArrayList<>();
            tags.forEach(item -> {
                // todo refactor
                String tag = item.text();
                Tag result = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tag));
                if (result == null) {
                    result = Tag.builder().name(tag).build();
                    tagMapper.insert(result);
                }
                tagsId.add(result.getId());
            });
            articleTagService.insertIgnoreArticleTags(entity.getId(), new HashSet<>(tagsId));

            log.info("Fetch article success, content:{}", JSONUtil.toJsonStr(entity));
        } catch (IOException e) {
            throw new FailedDependencyException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void psArticle(Article article) {
        baseMapper.updateById(article);
    }

    // todo config
    @Override
    public String randomImg() {
        return "https://picsum.photos/500/300?random=" + System.currentTimeMillis();
    }

    // todo
    private List<Long> getTagsList(ArticleDTO article) {
        List<Long> tagList = new ArrayList<>();
        article.getTags().forEach(item -> {
            Tag tags = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, item));
            if (tags == null) {
                tags = Tag.builder().name(item).sort(0).build();
                tagMapper.insert(tags);
            }
            tagList.add(tags.getId());
        });
        return tagList;
    }

    private Long getOrCreateCategory(String categoryName) {
        LambdaQueryWrapper<Category> categoryQuery = new LambdaQueryWrapper<>();
        categoryQuery.eq(Category::getName, categoryName)
                .last(SqlHelper.LIMIT_1);
        Category category = categoryMapper.selectOne(categoryQuery);
        if (Objects.nonNull(category)) {
            return category.getId();
        }
        category = Category.builder().name(categoryName).sort(0).build();
        categoryMapper.insert(category);
        return category.getId();
    }
}
