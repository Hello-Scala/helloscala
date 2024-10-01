package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateFriendLinkRequest;
import com.helloscala.admin.controller.request.BOUpdateFriendLinkRequest;
import com.helloscala.admin.controller.view.BOFriendLinkView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.FriendLinkService;
import com.helloscala.service.web.request.CreateFriendLinkRequest;
import com.helloscala.service.web.request.UpdateFriendLinkRequest;
import com.helloscala.service.web.view.FriendLinkView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author stevezou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOFriendLinkService {
    private final FriendLinkService friendLinkService;

    public Page<BOFriendLinkView> listByPage(String name, Integer status) {
        Page<?> page = PageUtil.getPage();
        Page<FriendLinkView> friendLinkPage = friendLinkService.selectFriendLinkPage(page, name, status);
        return PageHelper.convertTo(friendLinkPage, friendLink -> {
            BOFriendLinkView friendLinkView = new BOFriendLinkView();
            friendLinkView.setId(friendLink.getId());
            friendLinkView.setName(friendLink.getName());
            friendLinkView.setUrl(friendLink.getUrl());
            friendLinkView.setAvatar(friendLink.getAvatar());
            friendLinkView.setInfo(friendLink.getInfo());
            friendLinkView.setEmail(friendLink.getEmail());
            friendLinkView.setSort(friendLink.getSort());
            friendLinkView.setStatus(friendLink.getStatus());
            friendLinkView.setReason(friendLink.getReason());
            friendLinkView.setCreateTime(friendLink.getCreateTime());
            friendLinkView.setUpdateTime(friendLink.getUpdateTime());
            return friendLinkView;
        });
    }

    public void create(String userId, BOCreateFriendLinkRequest request) {
        CreateFriendLinkRequest createFriendLinkRequest = new CreateFriendLinkRequest();
        createFriendLinkRequest.setName(request.getName());
        createFriendLinkRequest.setUrl(request.getUrl());
        createFriendLinkRequest.setAvatar(request.getAvatar());
        createFriendLinkRequest.setInfo(request.getInfo());
        createFriendLinkRequest.setEmail(request.getEmail());
        createFriendLinkRequest.setSort(request.getSort());
        createFriendLinkRequest.setStatus(request.getStatus());
        createFriendLinkRequest.setReason(request.getReason());
        createFriendLinkRequest.setRequestBy(userId);
        friendLinkService.createFriendLink(createFriendLinkRequest);
    }

    public void update(String userId, BOUpdateFriendLinkRequest request) {
        UpdateFriendLinkRequest updateFriendLinkRequest = new UpdateFriendLinkRequest();
        updateFriendLinkRequest.setId(request.getId());
        updateFriendLinkRequest.setName(request.getName());
        updateFriendLinkRequest.setUrl(request.getUrl());
        updateFriendLinkRequest.setAvatar(request.getAvatar());
        updateFriendLinkRequest.setInfo(request.getInfo());
        updateFriendLinkRequest.setEmail(request.getEmail());
        updateFriendLinkRequest.setSort(request.getSort());
        updateFriendLinkRequest.setStatus(request.getStatus());
        updateFriendLinkRequest.setReason(request.getReason());
        updateFriendLinkRequest.setRequestBy(userId);
        friendLinkService.updateFriendLink(updateFriendLinkRequest);
    }

    public void bulkDelete(String userId, Set<String> ids) {
        friendLinkService.deleteFriendLink(ids);
        log.info("userId={}, deleted friendLink ids=[{}]", userId, String.join(",", ids));
    }

    public void top(String userId, String id) {
        friendLinkService.top(id);
        log.info("userId={}, topped friendLink id={}", userId, id);
    }
}
