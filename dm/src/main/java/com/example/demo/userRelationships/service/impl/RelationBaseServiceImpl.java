package com.example.demo.userRelationships.service.impl;

import com.example.demo.common.PlatformConstant;
import com.example.demo.userRelationships.dao.RelationBaseMapper;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.entity.UserRecordVo;
import com.example.demo.userRelationships.service.RelationBaseService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author MOONL
 * @version 2024/11/21
 *
 *  用户关系的基本信息service：关注人数、粉丝人数
 */

@Service
public class RelationBaseServiceImpl implements RelationBaseService {

    @Autowired
    private RelationBaseMapper relationBaseMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ApiResponse<String> getRelationCount(Long userId) {
        ApiResponse<String> response = new ApiResponse<>();
        Integer fansCount = relationBaseMapper.getFansCount(userId);
        Integer followingCount = relationBaseMapper.getFollowingCount(userId);
        response.setCode(200);
        response.setMessage("get relation count success");
        response.setData("user_id"+userId+"fansCount: " + fansCount + " followingCount: " + followingCount);
        return response;
    }

    @Override
    public User updateUser(User user) {
        this.relationBaseMapper.updateUser(user);
        return user;
    }

    @Override
    public UserRecordVo getUserRecord(String uid) {
        String userRecordKey = PlatformConstant.USER_RECORD + uid;
        UserRecordVo userRecordVo=new UserRecordVo();
        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey))) {
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
        }
        return userRecordVo;
    }
}
