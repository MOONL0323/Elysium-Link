package com.example.demo.userRelationships.service;

import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.entity.UserRecordVo;
import com.example.demo.util.ApiResponse;

public interface RelationBaseService {
    ApiResponse<String> getRelationCount(Long userId);

    /**
     * 更新一个用户信息
     *
     * @param user
     * @return
     */
    User updateUser(User user);

    /**
     * 得到用户的记录（新收到的点赞，关注，评论信息）
     *
     * @param uid
     * @return
     */
    UserRecordVo getUserRecord(String uid);
}
