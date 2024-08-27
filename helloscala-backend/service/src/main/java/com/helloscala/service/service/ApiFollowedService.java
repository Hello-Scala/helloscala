package com.helloscala.service.service;


public interface ApiFollowedService {
    void addFollowedUser(String userId);

    void deleteFollowed(String userId);
}
