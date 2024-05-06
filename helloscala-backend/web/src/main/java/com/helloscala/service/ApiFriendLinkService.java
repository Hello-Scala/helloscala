package com.helloscala.service;

import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FriendLink;

public interface ApiFriendLinkService {
    /**
     * 获取所有友链
     * @return
     */
    ResponseResult selectFriendLinkList();

    /**
     * 用户申请友链
     * @param friendLink
     * @return
     */
    ResponseResult addFriendLink(FriendLink friendLink);


}
