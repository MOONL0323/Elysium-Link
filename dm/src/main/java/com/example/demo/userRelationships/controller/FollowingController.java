package com.example.demo.userRelationships.controller;

import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.impl.FollowingServiceImpl;
import com.example.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 *Original by:
 * @author 86188
 *
 * Changed by:
 * @auther MOONL
 * @version 2024/11/21
 * - 更改url路径，并且userId从request中获取
 *
 * 关注Controller
 */
@RestController
@RequestMapping("/api/relation")
public class FollowingController {

    Logger logger = Logger.getLogger(FollowingController.class.getName());
    @Autowired
    private FollowingServiceImpl followingService;

    /**
     * 获取关注列表
     * @param request
     * @return
     */
    @GetMapping("/list/followee")
    public ApiResponse<List<User>> getFollowing(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return followingService.getFollowingInfo(userId);
    }

    /**
     * 关注
     * @param followingId
     * @param request
     */
    @PostMapping("/follow")
    public void addFollowing(@RequestBody Long followingId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        followingService.addFollowing(userId,followingId);
    }

    /**
     * 取消关注
     * @param followingId
     * @param request
     */
    @DeleteMapping("/cancel_follow")
    public void deleteFollowing(@RequestBody Long followingId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        followingService.deleteFollowing(userId, followingId);
    }

}
