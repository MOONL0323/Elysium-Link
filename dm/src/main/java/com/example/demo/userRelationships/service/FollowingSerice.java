package com.example.demo.userRelationships.service;

import java.util.List;

public interface FollowingSerice {
    List<Long> getFollowing(Long userId);

    void addFollowing(Long userId, Long followingId);

    void deleteFollowing(Long userId, Long followingId);
}
