package com.helloscala.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FriendLink;

import java.util.List;


public interface FriendLinkService extends IService<FriendLink> {
    ResponseResult selectFriendLinkPage(String name, Integer status);

    ResponseResult addFriendLink(FriendLink friendLink);

    ResponseResult updateFriendLink(FriendLink friendLink);

    ResponseResult deleteFriendLink(List<Integer> ids);

    ResponseResult top(Integer id);
}
