package com.example.demo.userRelationships.service;

import com.example.demo.userRelationships.entity.FollowingInfoResponse;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.util.ApiResponse;

import java.util.List;

public interface FollowingService {
    List<Long> getFollowing(Long userId);

    void addFollowing(Long userId, Long followingId);

    void deleteFollowing(Long userId, Long followingId);

    ApiResponse<FollowingInfoResponse> getFollowingInfo(Long userId);
}
