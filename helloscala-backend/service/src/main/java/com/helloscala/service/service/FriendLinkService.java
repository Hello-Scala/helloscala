package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.FriendLink;

import java.util.List;


public interface FriendLinkService extends IService<FriendLink> {
    Page<FriendLink> selectFriendLinkPage(String name, Integer status);

    void addFriendLink(FriendLink friendLink);

    void updateFriendLink(FriendLink friendLink);

    void deleteFriendLink(List<Integer> ids);

    void top(Integer id);
}
