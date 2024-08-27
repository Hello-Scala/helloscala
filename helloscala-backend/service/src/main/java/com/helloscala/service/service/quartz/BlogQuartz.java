package com.helloscala.service.service.quartz;

import cn.hutool.core.util.StrUtil;
import com.helloscala.service.entity.Article;
import com.helloscala.service.entity.Tag;
import com.helloscala.service.service.ArticleService;
import com.helloscala.service.service.RedisConstants;
import com.helloscala.service.service.RedisService;
import com.helloscala.service.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Component("blogQuartz")
@RequiredArgsConstructor
public class BlogQuartz {
    private final RedisService redisService;
    private final ArticleService articleService;
    private final TagService tagsService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        log.info(StrUtil.format("ryMultipleParams: String:{},Boolean:{}, Long:{}, Float:{}, Integer:{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        log.info("run with params:" + params);
    }

    public void ryNoParams() {
        log.info("run without params");
    }

    public void redisTimer() {
        redisService.redisTimer();
    }

    @SuppressWarnings("unchecked")
    public void updateReadQuantity() {
        List<Article> articles = new ArrayList<>();
        Map<String, Object> map = redisService.getCacheMap(RedisConstants.ARTICLE_READING);
        for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
            String id = stringEntry.getKey();
            List<String> list = (List<String>) stringEntry.getValue();
            Article article = Article.builder()
                    .id(id).quantity(list.size())
                    .build();
            articles.add(article);
        }
        articleService.updateBatchById(articles);
    }


    public void removeCodePassInIp() {
        redisService.deleteObject(RedisConstants.CHECK_CODE_IP);
    }

    public void updateTagsClickVolume() {
        Map<String, Object> map = redisService.getCacheMap(RedisConstants.TAG_CLICK_VOLUME);
        List<Tag> tagsList = new ArrayList<>();
        for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
            String id = stringEntry.getKey();
            Long value = (Long) stringEntry.getValue();
            Tag tags = new Tag(id, value);
            tagsList.add(tags);
        }
        tagsService.updateBatchById(tagsList);
    }
}
