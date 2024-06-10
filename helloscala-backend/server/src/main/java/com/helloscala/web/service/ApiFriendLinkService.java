package com.helloscala.web.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FriendLink;

public interface ApiFriendLinkService {
    ResponseResult selectFriendLinkList();

    ResponseResult addFriendLink(FriendLink friendLink);
}
