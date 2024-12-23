package com.example.demo.userRelationships.service;

import com.example.demo.userRelationships.entity.FollowingInfoResponse;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.util.ApiResponse;

import java.util.List;

public interface FollowerService {
    ApiResponse<FollowingInfoResponse> getFollowerInfo(Long userId);
    FollowingInfoResponse getFollowingInfoServer(Long userId);
}
