package com.example.demo.userRelationships.service;

import com.example.demo.userRelationships.entity.User;
import com.example.demo.util.ApiResponse;

import java.util.List;

public interface FollowingSerice {
    List<Long> getFollowing(Long userId);

    void addFollowing(Long userId, Long followingId);

    void deleteFollowing(Long userId, Long followingId);

    ApiResponse<List<User>> getFollowingInfo(Long userId);
}
