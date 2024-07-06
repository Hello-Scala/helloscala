package com.helloscala.common.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.ResultCode;
import com.helloscala.common.dto.article.ArticleDTO;
import com.helloscala.common.entity.Article;
import com.helloscala.common.entity.Category;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.enums.DataEventEnum;
import com.helloscala.common.enums.YesOrNoEnum;
import com.helloscala.common.event.DataEventPublisherService;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.CategoryMapper;
import com.helloscala.common.mapper.TagsMapper;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.service.ArticleService;
import com.helloscala.common.service.ArticleTagService;
import com.helloscala.common.service.util.ArticleEntityHelper;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.article.ArticleVO;
import com.helloscala.common.vo.user.SystemUserVO;
import com.helloscala.common.web.exception.FailedDependencyException;
import com.helloscala.common.web.exception.ForbiddenException;
import com.helloscala.common.web.exception.NotFoundException;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final TagsMapper tagsMapper;
    private final RestTemplate restTemplate;
    private final ArticleTagService articleTagService;
    private final DataEventPublisherService dataEventPublisherService;
    @Value("${baidu.url}")
    private String baiduUrl;

    @Override
    public Page<ArticleVO> selectArticlePage(String title, Integer tagId, Integer categoryId, Integer isPublish) {
        Page<ArticleVO> data = baseMapper.selectArticle(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), title, tagId, categoryId, isPublish);
        data.getRecords().forEach(item -> {
            SystemUserVO userInfo = userMapper.getById(item.getUserId());
            item.setNickname(userInfo.getNickname());
        });
        return data;
    }

    @Override
    public ArticleDTO selectArticleById(Long id) {
        ArticleDTO articleDTO = baseMapper.selectArticlePrimaryKey(id);
        articleDTO.setTags(tagsMapper.selectByArticleId(id));
        return articleDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(ArticleDTO article) {
        Article blogArticle = BeanCopyUtil.copyObject(article, Article.class);
        blogArticle.setUserId(StpUtil.getLoginIdAsString());
        Long categoryId = savaCategory(article.getCategoryName());
        List<Long> tagList = getTagsList(article);

        blogArticle.setCategoryId(categoryId);

        String ipAddress = IpUtil.getIp2region(IpUtil.getIp());
        if ("UNKNOWN".equals(ipAddress)) {
            blogArticle.setAddress("Earth");
        } else if (StringUtils.isNotBlank(ipAddress)) {
            String[] split = ipAddress.split("\\|");
            if (ipAddress.length() > 1) {
                String address = split[1];
                blogArticle.setAddress(address);
            } else {
                blogArticle.setAddress("Earth");
            }
        }
        int insert = baseMapper.insert(blogArticle);
        if (insert > 0) {
            articleTagService.insertIgnoreArticleTags(blogArticle.getId(), new HashSet<>(tagList));
        }

        //发布消息去同步es 不进行判断是否是发布状态了，因为后面修改成下架的话就还得去删除es里面的数据，多次一举了，在查询时添加条件发布状态为已发布
        dataEventPublisherService.publishData(DataEventEnum.ES_ADD_ARTICLE, ArticleEntityHelper.toElasticEntity(blogArticle));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleDTO article) {
        Article blogArticle = baseMapper.selectById(article.getId());
        if (ObjectUtil.isNull(blogArticle)) {
            throw new NotFoundException(ResultCode.ARTICLE_NOT_FOUND.desc);
        }
        String loginId = StpUtil.getLoginIdAsString();
        if (!blogArticle.getUserId().equals(loginId) && !StpUtil.hasRole(Constants.ADMIN_CODE)) {
            throw new ForbiddenException(ResultCode.NO_PERMISSION.desc);
        }

        Long categoryId = savaCategory(article.getCategoryName());
        List<Long> tagList = getTagsList(article);

        blogArticle = BeanCopyUtil.copyObject(article, Article.class);
        blogArticle.setCategoryId(categoryId);
        baseMapper.updateById(blogArticle);

        articleTagService.resetArticleTags(blogArticle.getId(), new HashSet<>(tagList));

        dataEventPublisherService.publishData(DataEventEnum.ES_UPDATE_ARTICLE, ArticleEntityHelper.toElasticEntity(blogArticle));
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
                Tag result = tagsMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tag));
                if (result == null) {
                    result = Tag.builder().name(tag).build();
                    tagsMapper.insert(result);
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

    private List<Long> getTagsList(ArticleDTO article) {
        List<Long> tagList = new ArrayList<>();
        article.getTags().forEach(item -> {
            Tag tags = tagsMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, item));
            if (tags == null) {
                tags = Tag.builder().name(item).sort(0).build();
                tagsMapper.insert(tags);
            }
            tagList.add(tags.getId());
        });
        return tagList;
    }

    private Long savaCategory(String categoryName) {
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, categoryName));
        if (category == null) {
            category = Category.builder().name(categoryName).sort(0).build();
            categoryMapper.insert(category);
        }
        return category.getId();
    }
}
