package com.helloscala.web.service.impl;


import cn.hutool.core.util.StrUtil;
import com.helloscala.common.RedisConstants;
import com.helloscala.common.entity.Tag;
import com.helloscala.common.entity.WebConfig;
import com.helloscala.common.mapper.ArticleMapper;
import com.helloscala.common.mapper.TagMapper;
import com.helloscala.common.mapper.WebConfigMapper;
import com.helloscala.common.service.RedisService;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.vo.article.ArticleVO;
import com.helloscala.common.vo.article.RecommendedArticleVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.web.response.GetHomeInfoResponse;
import com.helloscala.web.response.GetWebSiteInfoResponse;
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


@Service
@RequiredArgsConstructor
public class ApiHomeServiceImpl implements ApiHomeService {
    private static final String HOT_NEWS_URL_FMT = "https://www.coderutil.com/api/resou/v1/{}";
    private final RedisService redisService;
    private final WebConfigMapper webConfigMapper;
    private final ArticleMapper articleMapper;
    private final TagMapper tagMapper;

    public String report() {
        String ipAddress = IpUtil.getIp();
        HttpServletRequest request = IpUtil.getRequest();
        if (Objects.isNull(request)) {
            throw new BadRequestException("Failed to get request info!");
        }
        UserAgent userAgent = IpUtil.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        if (!redisService.sIsMember(RedisConstants.UNIQUE_VISITOR, md5)) {
            redisService.incr(RedisConstants.UNIQUE_VISITOR_COUNT, 1L);
            redisService.sAdd(RedisConstants.UNIQUE_VISITOR, md5);
        }
        redisService.incr(RedisConstants.BLOG_VIEWS_COUNT, 1L);
        return IpUtil.getIp2region(ipAddress);
    }

    @Override
    public GetHomeInfoResponse getHomeDataV2() {
        List<ArticleVO> articles = articleMapper.selectListByBanner();
        List<Tag> tags = tagMapper.selectList(null);
        List<RecommendedArticleVO> recommendedArticles = articleMapper.selectRecommendArticle();
        GetHomeInfoResponse response = new GetHomeInfoResponse();
        response.setBannerArticles(articles);
        response.setRecommendedArticles(recommendedArticles);
        response.setTags(tags);
        return response;
    }

    @Override
    public GetWebSiteInfoResponse getWebSiteInfoV2() {
        WebConfig webConfig = webConfigMapper.selectOne(null);
        Integer blogViewCount = (Integer) redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT);
        Long visitorCount = redisService.getCacheSetKeyNumber(RedisConstants.UNIQUE_VISITOR);

        GetWebSiteInfoResponse response = new GetWebSiteInfoResponse();
        response.setConfig(webConfig);
        response.setBlogViewCount(Long.valueOf(blogViewCount));
        response.setVisitorCount(visitorCount);
        return response;
    }

    @Override
    public String hot(String type) {
        return CustomHttpUtil.sendCuApiHttpUrl(StrUtil.format(HOT_NEWS_URL_FMT, type));
    }
}
