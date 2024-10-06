package com.helloscala.web.service;

import com.helloscala.service.service.FollowedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIFollowedService {
    private final FollowedService followedService;

    public void add(String userId, String followingUserId) {
        followedService.addFollowed(userId, followingUserId);
        log.info("Added followed, userId={}, followingUserId={}", userId, followingUserId);
    }

    public void deleteFollowed(String userId, String followingUserId) {
        followedService.deleteFollowed(userId, followingUserId);
        log.info("Deleted followed, userId={}, followingUserId={}", userId, followingUserId);
    }
}
