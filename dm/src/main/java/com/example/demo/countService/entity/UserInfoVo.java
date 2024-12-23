package com.example.demo.countService.entity;

import lombok.Data;

@Data
public class UserInfoVo {

    // 用户id
    private Long userId;

    // 关注数
    private Integer followingCount;

    // 粉丝数
    private Integer followerCount;

    // 文章数
    private Integer articleCount;

    // 用户热度
    private Integer userHeat;

}
