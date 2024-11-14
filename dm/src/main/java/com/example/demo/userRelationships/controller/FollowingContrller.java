package com.example.demo.userRelationships.controller;

import com.example.demo.userRelationships.service.impl.FollowingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 86188
 */
@RestController
@RequestMapping("/api/following")
public class FollowingContrller {
    @Autowired
    private FollowingServiceImpl followingService;

    @GetMapping("/{userId}")
    public List<Long> getFollowing(@PathVariable Long userId) {
        return followingService.getFollowing(userId);
    }

    @PostMapping("{userId}/add/{followId}")
    public void addFollowing(@PathVariable Long userId, @PathVariable Long followId) {
        followingService.addFollowing(userId, followId);
    }

    @DeleteMapping("{userId}/delete/{followingId}")
    public void deleteFollowing(@PathVariable Long userId, @PathVariable Long followingId) {
        followingService.deleteFollowing(userId, followingId);
    }

}
