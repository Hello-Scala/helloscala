package com.helloscala.quartz;

import cn.hutool.core.util.StrUtil;
import com.helloscala.common.RedisConstants;
import com.helloscala.entity.Article;
import com.helloscala.entity.Tags;
import com.helloscala.service.ArticleService;
import com.helloscala.service.RedisService;
import com.helloscala.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.helloscala.common.RedisConstants.ARTICLE_READING;
import static com.helloscala.common.RedisConstants.TAG_CLICK_VOLUME;

@Component("blogQuartz")
@RequiredArgsConstructor
public class BlogQuartz {
    private final RedisService redisService;

    private final ArticleService articleService;

    private final TagsService tagsService;


    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StrUtil.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    
    public void redisTimer(){
        redisService.redisTimer();
    }

    
    public void updateReadQuantity(){
        // 获取带阅读量的前缀key集合
        List<Article> articles = new ArrayList<>();
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_READING);
        // 取出所有数据更新到数据库
        for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
            String id = stringEntry.getKey();
            List<String> list = (List<String>) stringEntry.getValue();
            Article article = Article.builder()
                    .id(Long.parseLong(id)).quantity(list.size())
                    .build();
            articles.add(article);
        }
        articleService.updateBatchById(articles);
    }


    /**
     * 删除redis当天验证码通过的ip
     *
     */
    public void removeCodePassInIp(){
        redisService.deleteObject(RedisConstants.CHECK_CODE_IP);
    }

    /**
     * 每天定时修改标签的点击量
     *
     */
    public void updateTagsClickVolume(){
        Map<String, Object> map = redisService.getCacheMap(TAG_CLICK_VOLUME);
        List<Tags> tagsList = new ArrayList<>();
        for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
            String id = stringEntry.getKey();
            Integer value = (Integer) stringEntry.getValue();
            Tags tags = new Tags(Long.parseLong(id),value);
            tagsList.add(tags);
        }
        tagsService.updateBatchById(tagsList);
    }
}
