package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.FriendLink;
import com.helloscala.service.web.request.CreateFriendLinkRequest;
import com.helloscala.service.web.request.UpdateFriendLinkRequest;
import com.helloscala.service.web.view.FriendLinkView;

import java.util.Set;


public interface FriendLinkService extends IService<FriendLink> {
    Page<FriendLinkView> selectFriendLinkPage(Page<?> page, String name, Integer status);

    void createFriendLink(CreateFriendLinkRequest request);

    void updateFriendLink(UpdateFriendLinkRequest request);

    void deleteFriendLink(Set<String> ids);

    void top(String id);
}
