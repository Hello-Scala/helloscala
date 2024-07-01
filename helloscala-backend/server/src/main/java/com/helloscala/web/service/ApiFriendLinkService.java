package com.helloscala.web.service;

import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.vo.friendLink.ApiFriendLinkVO;

import java.util.List;

public interface ApiFriendLinkService {
    List<ApiFriendLinkVO> selectFriendLinkList();

    void addFriendLink(FriendLink friendLink);
}
