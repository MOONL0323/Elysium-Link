package com.example.demo.userRelationships.service.impl;

import com.example.demo.userRelationships.dao.FollowingMapper;
import com.example.demo.userRelationships.dao.UserMapper;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowerService;
import com.example.demo.userRelationships.service.FollowingSerice;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Original by
 * @Author 86188
 *
 * Changed by
 * @Author MOONL
 * @version 2024/11/21
 * - 更改url路径，并且userId从request中获取
 * - 更改返回值类型为统一的ApiResponse格式
 * - 增加了getFollowingInfo方法，用来获取关注列表的用户具体信息
 *
 *  关注的service
 */
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

    /**
     * 获取关注列表
     * @param userId
     * @return ApiResponse<List<User>>
     */
    @Override
    public ApiResponse<List<User>> getFollowingInfo(Long userId) {
        List<Long> followingIds = getFollowing(userId);
        List<User> followings = followingIds.stream().map(followingId -> {
            //去mapper中查找
            User following = userDao.findByUserId(followingId);
            return following;
        }).toList();

        ApiResponse<List<User>> response = new ApiResponse<>();
        response.setData(followings);
        response.setMessage("获取关注列表成功");
        response.setCode(200);
        return response;
    }
}
