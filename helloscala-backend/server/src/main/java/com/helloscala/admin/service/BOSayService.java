package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateSayRequest;
import com.helloscala.admin.controller.request.BOUpdateSayRequest;
import com.helloscala.admin.controller.view.BOSayView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.SayService;
import com.helloscala.service.web.request.CreateSayRequest;
import com.helloscala.service.web.request.UpdateSayRequest;
import com.helloscala.service.web.view.SayView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author stevezou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOSayService {
    private final SayService sayService;

    public Page<BOSayView> listByPage(String keywords) {
        Page<?> page = PageUtil.getPage();
        Page<SayView> sayViewPage = sayService.selectSayPage(page, keywords);
        return PageHelper.convertTo(sayViewPage, BOSayService::buildBoSayView);
    }

    private static @NotNull BOSayView buildBoSayView(SayView say) {
        BOSayView sayView = new BOSayView();
        sayView.setId(say.getId());
        sayView.setUserId(say.getUserId());
        sayView.setImgUrl(say.getImgUrl());
        sayView.setContent(say.getContent());
        sayView.setAddress(say.getAddress());
        sayView.setIsPublic(say.getIsPublic());
        sayView.setCreateTime(say.getCreateTime());
        sayView.setUpdateTime(say.getUpdateTime());
        return sayView;
    }

    public BOSayView getById(String id) {
        SayView sayView = sayService.selectSayById(id);
        return buildBoSayView(sayView);
    }

    public void update(String userId, BOUpdateSayRequest request) {
        UpdateSayRequest updateSayRequest = new UpdateSayRequest();
        updateSayRequest.setId(request.getId());
        updateSayRequest.setUserId(request.getUserId());
        updateSayRequest.setImgUrl(request.getImgUrl());
        updateSayRequest.setContent(request.getContent());
        updateSayRequest.setAddress(request.getAddress());
        updateSayRequest.setIsPublic(request.getIsPublic());
        updateSayRequest.setRequestBy(userId);
        sayService.updateSay(updateSayRequest);
    }

    public void create(String userId, BOCreateSayRequest request) {
        CreateSayRequest createSayRequest = new CreateSayRequest();
        createSayRequest.setUserId(userId);
        createSayRequest.setImgUrl(request.getImgUrl());
        createSayRequest.setContent(request.getContent());
        createSayRequest.setAddress(request.getAddress());
        createSayRequest.setIsPublic(request.getIsPublic());
        sayService.addSay(createSayRequest);
    }

    public void bulkDelete(String userId, Set<String> ids) {
        sayService.deleteSay(ids);
        log.info("userId={}, deleted Day ids=[{}]", userId, String.join(",", ids));
    }
}
