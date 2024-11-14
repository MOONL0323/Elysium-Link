package com.example.demo.userRelationships.service;

import java.util.List;

public interface FollowerService {
    List<Long> getFollower(Long userId);
    void addFollower(Long userId, Long followerId);
    void deleteFollower(Long userId, Long followerId);
}
