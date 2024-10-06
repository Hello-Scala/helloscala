package com.helloscala.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helloscala.service.entity.Followed;

import java.util.List;


public interface FollowedService extends IService<Followed> {
    List<String> listFollowedUserIds(String userId);

    void addFollowed(String userId, String followingUserId);

    void deleteFollowed(String userId, String followingUserId);
}
