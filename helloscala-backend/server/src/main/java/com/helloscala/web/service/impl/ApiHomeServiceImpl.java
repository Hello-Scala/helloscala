package com.helloscala.web.service.impl;


import com.helloscala.common.RedisConstants;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Tags;
import com.helloscala.common.entity.WebConfig;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.TagsMapper;
import com.helloscala.common.mapper.WebConfigMapper;
import com.helloscala.common.service.RedisService;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.vo.article.ApiArticleListVO;
import com.helloscala.common.vo.article.SystemArticleListVO;
import com.helloscala.web.service.ApiHomeService;
import com.helloscala.web.utils.CustomHttpUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ApiHomeServiceImpl implements ApiHomeService {
    private final RedisService redisService;
    private final WebConfigMapper webConfigMapper;
    private final ArticleMapper articleMapper;
    private final TagsMapper tagsMapper;

    public ResponseResult report() {
        String ipAddress = IpUtil.getIp();
        HttpServletRequest request = IpUtil.getRequest();
        if (Objects.isNull(request)) {
            return ResponseResult.error("Failed to get request info!");
        }
        UserAgent userAgent = IpUtil.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        if (!redisService.sIsMember(RedisConstants.UNIQUE_VISITOR, md5)) {
            redisService.incr(RedisConstants.UNIQUE_VISITOR_COUNT, 1);
            redisService.sAdd(RedisConstants.UNIQUE_VISITOR, md5);
        }
        redisService.incr(RedisConstants.BLOG_VIEWS_COUNT, 1);
        return ResponseResult.success(IpUtil.getIp2region(ipAddress));
    }

    public ResponseResult getHomeData() {
        List<SystemArticleListVO> articles = articleMapper.selectListByBanner();
        List<Tags> tags = tagsMapper.selectList(null);
        List<ApiArticleListVO> apiArticleListVOS = articleMapper.selectRecommendArticle();
        return ResponseResult.success().putExtra("articles",articles).putExtra("newArticleList",apiArticleListVOS).putExtra("tagCloud",tags);
    }

    public ResponseResult getWebSiteInfo() {
        WebConfig webConfig = webConfigMapper.selectOne(null);
        Object count = redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT);
        Long visitorAccess = redisService.getCacheSetKeyNumber(RedisConstants.UNIQUE_VISITOR);
        return ResponseResult.success(webConfig).putExtra("siteAccess", Optional.ofNullable(count).orElse(0))
                .putExtra("visitorAccess",visitorAccess);
    }

    @Override
    public ResponseResult hot(String type) {
        String url = "https://www.coderutil.com/api/resou/v1/" + type;
        String result = CustomHttpUtil.sendCuApiHttpUrl(url);
        return ResponseResult.success(result);
    }
}
