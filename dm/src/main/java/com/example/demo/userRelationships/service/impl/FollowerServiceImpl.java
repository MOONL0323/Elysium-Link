package com.example.demo.userRelationships.service.impl;

import com.example.demo.userRelationships.dao.FollowerDao;
import com.example.demo.userRelationships.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FollowerServiceImpl implements FollowerService {
    @Autowired
    private FollowerDao followerDao;

    @Override
    public List<Long> getFollower(Long userId) {
        return followerDao.getFollower(userId);
    }

    @Override
    public void addFollower(Long userId, Long followerId) {
        followerDao.addFollower(userId, followerId);
    }

    @Override
    public void deleteFollower(Long userId, Long followerId) {
        followerDao.deleteFollower(userId, followerId);
    }
}
