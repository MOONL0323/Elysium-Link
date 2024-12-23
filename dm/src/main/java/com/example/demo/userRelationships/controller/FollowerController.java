package com.example.demo.userRelationships.controller;

import com.example.demo.userRelationships.entity.FollowingInfoResponse;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowerService;
import com.example.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Original by
 * @author 86188
 *
 * Changed by
 * @author MOONL
 * @version 2024/11/21
 * - 更改url路径，并且userId从request中获取
 *
 *  被关注的controller
 */
@RestController
@RequestMapping("/api/relation")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    // 获取粉丝列表
    @GetMapping("/list/follower")
    public ApiResponse<FollowingInfoResponse> getFollower(HttpServletRequest request) {
        // 从request中获取userId，不再使用url中的userId
        Long userId = Long.valueOf((String) request.getAttribute("userId"));
        return followerService.getFollowerInfo(userId);
    }

}
