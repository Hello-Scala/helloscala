package com.helloscala.web.service;

import cn.hutool.core.util.StrUtil;
import com.helloscala.common.cache.RedisService;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.SqlHelper;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.service.service.ArticleService;
import com.helloscala.service.service.TagService;
import com.helloscala.service.service.WebConfigService;
import com.helloscala.service.web.request.SearchBannerArticleRequest;
import com.helloscala.service.web.request.SortingRule;
import com.helloscala.service.web.view.ArticleView;
import com.helloscala.service.web.view.BannerArticleView;
import com.helloscala.service.web.view.TagView;
import com.helloscala.service.web.view.WebConfigView;
import com.helloscala.web.controller.home.*;
import com.helloscala.web.utils.CustomHttpUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIHomeService {
    private static final String HOT_NEWS_URL_FMT = "https://www.coderutil.com/api/resou/v1/{}";
    private final WebConfigService webConfigService;
    private final RedisService redisService;
    private final ArticleService articleService;
    private final TagService tagService;

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
        if (!redisService.isMember(RedisConstants.UNIQUE_VISITOR, md5)) {
            redisService.incr(RedisConstants.UNIQUE_VISITOR_COUNT, 1L);
            redisService.sAdd(RedisConstants.UNIQUE_VISITOR, md5);
        }
        redisService.incr(RedisConstants.BLOG_VIEWS_COUNT, 1L);
        return IpUtil.getIp2region(ipAddress);
    }

    public APIGetWebSiteInfoResponse getWebSiteInfoV2() {
        WebConfigView webConfig = webConfigService.getWebConfig();
        Integer blogViewCount = (Integer) redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT);
        Long visitorCount = redisService.getCacheSetKeyNumber(RedisConstants.UNIQUE_VISITOR);

        APIGetWebSiteInfoResponse response = new APIGetWebSiteInfoResponse();
        response.setConfig(buildAPIWebConfigView(webConfig));
        response.setBlogViewCount(Long.valueOf(blogViewCount));
        response.setVisitorCount(visitorCount);
        return response;
    }

    public APIGetHomeInfoResponse getHomeData() {
        SortingRule byCreateTime = new SortingRule(SqlHelper.getFieldName(ArticleView::getCreateTime));
        SortingRule byIsStick = new SortingRule(SqlHelper.getFieldName(ArticleView::getIsStick));

        SearchBannerArticleRequest searchBannerArticleRequest = new SearchBannerArticleRequest();
        searchBannerArticleRequest.setIsPublish(1);
        searchBannerArticleRequest.setIsCarousel(1);
        searchBannerArticleRequest.setSortingRules(List.of(byIsStick, byCreateTime));
        List<BannerArticleView> bannerArticles = articleService.searchBanner(searchBannerArticleRequest);
        List<APIBannerArticleView> bannerArticleViews = bannerArticles.stream().map(APIHomeService::buildAPIBannerArticleView).toList();

        List<TagView> tags = tagService.listAllTags();
        List<APITagView> tagViews = tags.stream().map(APIHomeService::buildAPITagView).toList();

        SearchBannerArticleRequest searchRecommendArticleRequest = new SearchBannerArticleRequest();
        searchRecommendArticleRequest.setIsRecommend(1);
        searchRecommendArticleRequest.setSortingRules(List.of(byCreateTime));
        List<BannerArticleView> recommendArticles = articleService.searchBanner(searchRecommendArticleRequest);
        List<APIBannerArticleView> recommendArticleViews = recommendArticles.stream().map(APIHomeService::buildAPIBannerArticleView).toList();

        APIGetHomeInfoResponse response = new APIGetHomeInfoResponse();
        response.setBannerArticles(bannerArticleViews);
        response.setRecommendedArticles(recommendArticleViews);
        response.setTags(tagViews);
        return response;
    }

    private static @NotNull APITagView buildAPITagView(TagView tag) {
        APITagView tagView = new APITagView();
        tagView.setId(tag.getId());
        tagView.setName(tag.getName());
        tagView.setSort(tag.getSort());
        tagView.setClickVolume(tag.getClickVolume());
        tagView.setCreateTime(tag.getCreateTime());
        tagView.setUpdateTime(tag.getUpdateTime());
        return tagView;
    }

    private static @NotNull APIBannerArticleView buildAPIBannerArticleView(BannerArticleView article) {
        APIBannerArticleView bannerArticleView = new APIBannerArticleView();
        bannerArticleView.setId(article.getId());
        bannerArticleView.setTitle(article.getTitle());
        bannerArticleView.setAvatar(article.getAvatar());
        bannerArticleView.setCreateTime(article.getCreateTime());
        return bannerArticleView;
    }

    private APIWebConfigView buildAPIWebConfigView(WebConfigView webConfig) {
        APIWebConfigView webConfigView = new APIWebConfigView();
        webConfigView.setId(webConfig.getId());
        webConfigView.setLogo(webConfig.getLogo());
        webConfigView.setName(webConfig.getName());
        webConfigView.setWebUrl(webConfig.getWebUrl());
        webConfigView.setSummary(webConfig.getSummary());
        webConfigView.setKeyword(webConfig.getKeyword());
        webConfigView.setAuthor(webConfig.getAuthor());
        webConfigView.setRecordNum(webConfig.getRecordNum());
        webConfigView.setCreateTime(webConfig.getCreateTime());
        webConfigView.setUpdateTime(webConfig.getUpdateTime());
        webConfigView.setAliPay(webConfig.getAliPay());
        webConfigView.setWeixinPay(webConfig.getWeixinPay());
        webConfigView.setGithub(webConfig.getGithub());
        webConfigView.setGitee(webConfig.getGitee());
        webConfigView.setQqNumber(webConfig.getQqNumber());
        webConfigView.setQqGroup(webConfig.getQqGroup());
        webConfigView.setEmail(webConfig.getEmail());
        webConfigView.setWechat(webConfig.getWechat());
        webConfigView.setShowList(webConfig.getShowList());
        webConfigView.setLoginTypeList(webConfig.getLoginTypeList());
        webConfigView.setOpenComment(webConfig.getOpenComment());
        webConfigView.setOpenAdmiration(webConfig.getOpenAdmiration());
        webConfigView.setAuthorInfo(webConfig.getAuthorInfo());
        webConfigView.setAuthorAvatar(webConfig.getAuthorAvatar());
        webConfigView.setTouristAvatar(webConfig.getTouristAvatar());
        webConfigView.setBulletin(webConfig.getBulletin());
        webConfigView.setShowBulletin(webConfig.getShowBulletin());
        webConfigView.setAboutMe(webConfig.getAboutMe());
        webConfigView.setIsMusicPlayer(webConfig.getIsMusicPlayer());
        return webConfigView;
    }

    public String hotNews(String type) {
        return CustomHttpUtil.sendCuApiHttpUrl(StrUtil.format(HOT_NEWS_URL_FMT, type));
    }
}
