package com.example.demo.userRelationships.service.impl;

import com.example.demo.userRelationships.dao.FollowingDao;
import com.example.demo.userRelationships.dao.UserDao;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowerService;
import com.example.demo.userRelationships.service.FollowingSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Component
@Service
public class FollowingServiceImpl implements FollowingSerice {
    @Autowired
    private FollowingDao followingDao;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserDao userDao;

    @Override
    public List<Long> getFollowing(Long userId) {
        return followingDao.getFollowing(userId);
    }

    @Override
    public void addFollowing(Long userId, Long followingId) {
        User user = userDao.findByUserId(userId);
        User followUser = userDao.findByUserId(followingId);
        if(user!=null&&followUser!=null){
            followingDao.addFollowing(userId, followingId);
            followerService.addFollower(followingId, userId);
        }else{
            System.out.println("用户不存在");
        }
    }

    @Override
    public void deleteFollowing(Long userId, Long followingId) {
        List<Long> followingList = followingDao.getFollowing(userId);
        if(followingList.contains(followingId)){
            followingDao.deleteFollowing(userId,followingId);
            followerService.deleteFollower(followingId,userId);
        }else{
            System.out.println("未关注该用户");
        }
    }
}
