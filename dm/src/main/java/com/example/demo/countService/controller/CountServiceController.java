package com.example.demo.countService.controller;

import com.example.demo.countService.countServiceServer.CountServiceServer;
import com.example.demo.countService.entity.ContentInfoVo;
import com.example.demo.countService.entity.UserInfoVo;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 计数服务Controller
 * @auther MOONL
 * @version 2024/12/01
 */

@RestController
@RequestMapping("/api/relation")
public class CountServiceController {

    @Autowired
    private CountServiceServer countServiceServer;

    /**
     * 获取用户维度的信息，用户id，关注数，粉丝数，文章数
     * @param userId
     * @return
     */
    @GetMapping("/state")
    public ApiResponse<UserInfoVo> getUserInfo(Long userId) {
        return countServiceServer.getUserInfo(userId);
    }



}
