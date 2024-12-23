package com.example.demo.countService.countServiceServer;

import com.example.demo.countService.entity.ContentInfoVo;
import com.example.demo.countService.entity.UserInfoVo;
import com.example.demo.util.ApiResponse;

public interface CountServiceServer {
    ApiResponse<UserInfoVo> getUserInfo(Long userId);
    boolean incrUserInfo(Long userId, String field);
    boolean decrUserInfo(Long userId, String field);
    boolean setUserInfo(Long userId, String... fieldsAndValues);
    boolean setUserInfo(Long userId, UserInfoVo userInfoVo);
    Integer userHeat(Integer followerCount, Integer articleCount);
    Integer getFollowerCount(Long userId);

}
