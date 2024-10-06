package com.helloscala.web.service;

import com.helloscala.service.service.FriendLinkService;
import com.helloscala.service.web.request.CreateFriendLinkRequest;
import com.helloscala.service.web.view.FriendLinkView;
import com.helloscala.web.controller.friendlink.APICreateFriendLinkRequest;
import com.helloscala.web.controller.friendlink.APIFriendLinkView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIFriendLinkService {
    private final FriendLinkService friendLinkService;

    public List<APIFriendLinkView> list() {
        List<FriendLinkView> friendLinkViews = friendLinkService.listVisible();
        return friendLinkViews.stream().map(friendLink -> {
            APIFriendLinkView friendLinkView = new APIFriendLinkView();
            friendLinkView.setId(friendLink.getId());
            friendLinkView.setName(friendLink.getName());
            friendLinkView.setUrl(friendLink.getUrl());
            friendLinkView.setAvatar(friendLink.getAvatar());
            friendLinkView.setInfo(friendLink.getInfo());
            return friendLinkView;
        }).toList();
    }

    public void create(String userId, APICreateFriendLinkRequest request) {
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
}
