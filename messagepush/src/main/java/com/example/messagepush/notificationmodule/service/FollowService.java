// FollowService.java
package com.example.messagepush.notificationmodule.service;

import com.example.messagepush.notificationmodule.mapper.FollowMapper;
import com.example.messagepush.notificationmodule.model.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    @Autowired
    private FollowMapper followMapper;

    public void followUser(Long userId, Long followId) {
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowId(followId);
        followMapper.insertFollow(follow);
    }

    public void unfollowUser(Long userId, Long followId) {
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowId(followId);
        followMapper.deleteFollow(follow);
    }

    public List<Long> getFollowedUsers(Long userId) {
        return followMapper.findFollowsByUserId(userId);
    }

    public List<Long> getFollowers(Long userId) {
        return followMapper.findFollowersByUserId(userId);
    }
}