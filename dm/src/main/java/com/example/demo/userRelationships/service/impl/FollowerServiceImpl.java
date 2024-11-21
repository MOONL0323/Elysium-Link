package com.example.demo.userRelationships.service.impl;

import com.example.demo.userRelationships.dao.FollowerMapper;
import com.example.demo.userRelationships.dao.UserMapper;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowerService;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FollowerServiceImpl implements FollowerService {
    @Autowired
    private FollowerMapper followerMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Long> getFollower(Long userId) {
        return followerMapper.getFollower(userId);
    }

    @Override
    public void addFollower(Long userId, Long followerId) {
        followerMapper.addFollower(userId, followerId);
    }

    @Override
    public void deleteFollower(Long userId, Long followerId) {
        followerMapper.deleteFollower(userId, followerId);
    }

    @Override
    public ApiResponse<List<User>> getFollowerInfo(Long userId) {
        List<Long> followerIds = getFollower(userId);
        List<User> followers = followerIds.stream().map(followerId -> {
            //去mapper中查找
            User follower = userMapper.findByUserId(followerId);
            return follower;
        }).toList();

        ApiResponse<List<User>> response = new ApiResponse<>();
        response.setData(followers);
        response.setMessage("获取粉丝列表成功");
        response.setCode(200);
        return response;
    }
}
