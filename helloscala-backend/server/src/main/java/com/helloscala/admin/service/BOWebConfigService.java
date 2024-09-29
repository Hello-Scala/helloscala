package com.helloscala.admin.service;

import com.helloscala.admin.controller.request.BOUpdateWebConfigRequest;
import com.helloscala.admin.controller.view.BOWebConfigView;
import com.helloscala.service.service.WebConfigService;
import com.helloscala.service.web.request.UpdateWebConfigRequest;
import com.helloscala.service.web.view.WebConfigView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author stevezou
 */
@Service
@RequiredArgsConstructor
public class BOWebConfigService {
    private final WebConfigService webConfigService;

    public BOWebConfigView get() {
        WebConfigView webConfig = webConfigService.getWebConfig();
        BOWebConfigView webConfigView = new BOWebConfigView();
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

    public void update(String userId, BOUpdateWebConfigRequest request) {
        UpdateWebConfigRequest updateWebConfigRequest = new UpdateWebConfigRequest();
        updateWebConfigRequest.setId(request.getId());
        updateWebConfigRequest.setLogo(request.getLogo());
        updateWebConfigRequest.setName(request.getName());
        updateWebConfigRequest.setWebUrl(request.getWebUrl());
        updateWebConfigRequest.setSummary(request.getSummary());
        updateWebConfigRequest.setKeyword(request.getKeyword());
        updateWebConfigRequest.setAuthor(request.getAuthor());
        updateWebConfigRequest.setRecordNum(request.getRecordNum());
        updateWebConfigRequest.setCreateTime(request.getCreateTime());
        updateWebConfigRequest.setUpdateTime(request.getUpdateTime());
        updateWebConfigRequest.setAliPay(request.getAliPay());
        updateWebConfigRequest.setWeixinPay(request.getWeixinPay());
        updateWebConfigRequest.setGithub(request.getGithub());
        updateWebConfigRequest.setGitee(request.getGitee());
        updateWebConfigRequest.setQqNumber(request.getQqNumber());
        updateWebConfigRequest.setQqGroup(request.getQqGroup());
        updateWebConfigRequest.setEmail(request.getEmail());
        updateWebConfigRequest.setWechat(request.getWechat());
        updateWebConfigRequest.setShowList(request.getShowList());
        updateWebConfigRequest.setLoginTypeList(request.getLoginTypeList());
        updateWebConfigRequest.setOpenComment(request.getOpenComment());
        updateWebConfigRequest.setOpenAdmiration(request.getOpenAdmiration());
        updateWebConfigRequest.setAuthorInfo(request.getAuthorInfo());
        updateWebConfigRequest.setAuthorAvatar(request.getAuthorAvatar());
        updateWebConfigRequest.setTouristAvatar(request.getTouristAvatar());
        updateWebConfigRequest.setBulletin(request.getBulletin());
        updateWebConfigRequest.setShowBulletin(request.getShowBulletin());
        updateWebConfigRequest.setAboutMe(request.getAboutMe());
        updateWebConfigRequest.setIsMusicPlayer(request.getIsMusicPlayer());
        updateWebConfigRequest.setRequestBy(userId);
        webConfigService.updateWebConfig(updateWebConfigRequest);
    }
}
