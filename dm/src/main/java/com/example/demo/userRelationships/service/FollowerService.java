package com.example.demo.userRelationships.service;

import com.example.demo.userRelationships.entity.User;
import com.example.demo.util.ApiResponse;

import java.util.List;

public interface FollowerService {
    List<Long> getFollower(Long userId);
    void addFollower(Long userId, Long followerId);
    void deleteFollower(Long userId, Long followerId);
    ApiResponse<List<User>> getFollowerInfo(Long userId);
}
