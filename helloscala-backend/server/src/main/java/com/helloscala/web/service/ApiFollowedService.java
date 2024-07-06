package com.helloscala.web.service;


public interface ApiFollowedService {
    void addFollowedUser(String userId);

    void deleteFollowed(String userId);
}
