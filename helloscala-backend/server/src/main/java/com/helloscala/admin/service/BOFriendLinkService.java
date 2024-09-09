package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOAddFriendLinkRequest;
import com.helloscala.admin.controller.request.BOUpdateFriendLinkRequest;
import com.helloscala.admin.controller.view.BOFriendLinkView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.FriendLinkService;
import com.helloscala.service.web.request.AddFriendLinkRequest;
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
        Page<FriendLinkView> friendLinkViewPage = friendLinkService.selectFriendLinkPage(page, name, status);
        return PageHelper.convertTo(friendLinkViewPage, friendLinkView -> {
            BOFriendLinkView boFriendLinkView = new BOFriendLinkView();
            boFriendLinkView.setId(friendLinkView.getId());
            boFriendLinkView.setName(friendLinkView.getName());
            boFriendLinkView.setUrl(friendLinkView.getUrl());
            boFriendLinkView.setAvatar(friendLinkView.getAvatar());
            boFriendLinkView.setInfo(friendLinkView.getInfo());
            boFriendLinkView.setEmail(friendLinkView.getEmail());
            boFriendLinkView.setSort(friendLinkView.getSort());
            boFriendLinkView.setStatus(friendLinkView.getStatus());
            boFriendLinkView.setReason(friendLinkView.getReason());
            boFriendLinkView.setCreateTime(friendLinkView.getCreateTime());
            boFriendLinkView.setUpdateTime(friendLinkView.getUpdateTime());
            return boFriendLinkView;
        });
    }

    public void create(String userId, BOAddFriendLinkRequest request) {
        AddFriendLinkRequest addFriendLinkRequest = new AddFriendLinkRequest();
        addFriendLinkRequest.setName(request.getName());
        addFriendLinkRequest.setUrl(request.getUrl());
        addFriendLinkRequest.setAvatar(request.getAvatar());
        addFriendLinkRequest.setInfo(request.getInfo());
        addFriendLinkRequest.setEmail(request.getEmail());
        addFriendLinkRequest.setSort(request.getSort());
        addFriendLinkRequest.setStatus(request.getStatus());
        addFriendLinkRequest.setReason(request.getReason());
        addFriendLinkRequest.setRequestBy(userId);
        friendLinkService.addFriendLink(addFriendLinkRequest);
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
