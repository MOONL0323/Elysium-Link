package com.example.demo.userRelationships.service.impl;

import com.example.demo.userRelationships.dao.FollowerMapper;
import com.example.demo.userRelationships.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FollowerServiceImpl implements FollowerService {
    @Autowired
    private FollowerMapper followerMapper;

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
}
