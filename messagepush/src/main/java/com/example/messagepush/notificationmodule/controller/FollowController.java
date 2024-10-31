package com.example.messagepush.notificationmodule.controller;

import com.example.messagepush.notificationmodule.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/{userId}/followed")
    public List<Long> getFollowedUsers(@PathVariable Long userId) {
        return followService.getFollowedUsers(userId);
    }

    @GetMapping("/{userId}/followers")
    public List<Long> getFollowers(@PathVariable Long userId) {
        // 实现获取粉丝列表的逻辑
        return followService.getFollowers(userId);
    }
}