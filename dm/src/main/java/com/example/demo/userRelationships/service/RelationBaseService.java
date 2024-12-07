package com.example.demo.userRelationships.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.entity.UserRecordVo;
import com.example.demo.util.ApiResponse;
import org.springframework.stereotype.Service;


public interface RelationBaseService extends IService<User> {
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
