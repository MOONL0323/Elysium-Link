// NotificationService.java
package com.example.messagepush.notificationmodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private FollowService followService;

    public void notifyFollowers(Long userId, String content) {
        List<Long> followers = followService.getFollowedUsers(userId);
        for (Long followerId : followers) {
            // 模拟推送通知
            System.out.println("通知用户 " + followerId + ": 用户 " + userId + " 发布了新的动态 - " + content);
        }
    }
}