package com.helloscala.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helloscala.common.Constants;
import com.helloscala.common.web.exception.NotFoundException;
import com.helloscala.service.entity.WebConfig;
import com.helloscala.service.mapper.WebConfigMapper;
import com.helloscala.service.service.WebConfigService;
import com.helloscala.service.web.request.UpdateWebConfigRequest;
import com.helloscala.service.web.view.WebConfigView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {
    @Override
    public WebConfigView getWebConfig() {
        LambdaQueryWrapper<WebConfig> queryWrapper = new LambdaQueryWrapper<WebConfig>()
                .orderByDesc(WebConfig::getCreateTime)
                .last(Constants.LIMIT_ONE);
        WebConfig webConfig = baseMapper.selectOne(queryWrapper);

        WebConfigView webConfigView = new WebConfigView();
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWebConfig(UpdateWebConfigRequest request) {
        WebConfig webConfig = baseMapper.selectById(request.getId());
        if (Objects.isNull(webConfig)) {
            throw new NotFoundException("WebConfig not found, id={}!", request.getId());
        }
        webConfig.setLogo(request.getLogo());
        webConfig.setName(request.getName());
        webConfig.setWebUrl(request.getWebUrl());
        webConfig.setSummary(request.getSummary());
        webConfig.setKeyword(request.getKeyword());
        webConfig.setAuthor(request.getAuthor());
        webConfig.setRecordNum(request.getRecordNum());
        webConfig.setCreateTime(request.getCreateTime());
        webConfig.setUpdateTime(request.getUpdateTime());
        webConfig.setAliPay(request.getAliPay());
        webConfig.setWeixinPay(request.getWeixinPay());
        webConfig.setGithub(request.getGithub());
        webConfig.setGitee(request.getGitee());
        webConfig.setQqNumber(request.getQqNumber());
        webConfig.setQqGroup(request.getQqGroup());
        webConfig.setEmail(request.getEmail());
        webConfig.setWechat(request.getWechat());
        webConfig.setShowList(request.getShowList());
        webConfig.setLoginTypeList(request.getLoginTypeList());
        webConfig.setOpenComment(request.getOpenComment());
        webConfig.setOpenAdmiration(request.getOpenAdmiration());
        webConfig.setAuthorInfo(request.getAuthorInfo());
        webConfig.setAuthorAvatar(request.getAuthorAvatar());
        webConfig.setTouristAvatar(request.getTouristAvatar());
        webConfig.setBulletin(request.getBulletin());
        webConfig.setShowBulletin(request.getShowBulletin());
        webConfig.setAboutMe(request.getAboutMe());
        webConfig.setIsMusicPlayer(request.getIsMusicPlayer());
        baseMapper.updateById(webConfig);
    }
}
