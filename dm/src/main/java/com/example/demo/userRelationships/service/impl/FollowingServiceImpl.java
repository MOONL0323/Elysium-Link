package com.example.demo.userRelationships.service.impl;

import com.example.demo.userRelationships.dao.FollowingMapper;
import com.example.demo.userRelationships.dao.UserMapper;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowerService;
import com.example.demo.userRelationships.service.FollowingSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingServiceImpl implements FollowingSerice {
    @Autowired
    private FollowingMapper followingMapper;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserMapper userDao;

    @Override
    public List<Long> getFollowing(Long userId) {
        return followingMapper.getFollowing(userId);
    }

    @Override
    public void addFollowing(Long userId, Long followingId) {
        User user = userDao.findByUserId(userId);
        User followUser = userDao.findByUserId(followingId);
        if(user!=null&&followUser!=null){
            followingMapper.addFollowing(userId, followingId);
            followerService.addFollower(followingId, userId);
        }else{
            System.out.println("用户不存在");
        }
    }

    @Override
    public void deleteFollowing(Long userId, Long followingId) {
        List<Long> followingList = followingMapper.getFollowing(userId);
        if(followingList.contains(followingId)){
            followingMapper.deleteFollowing(userId,followingId);
            followerService.deleteFollower(followingId,userId);
        }else{
            System.out.println("未关注该用户");
        }
    }
}
