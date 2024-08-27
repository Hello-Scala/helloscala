package com.helloscala.service.service;

import com.helloscala.service.entity.FriendLink;
import com.helloscala.common.vo.friendLink.ApiFriendLinkVO;

import java.util.List;

public interface ApiFriendLinkService {
    List<ApiFriendLinkVO> selectFriendLinkList();

    void addFriendLink(FriendLink friendLink);
}
