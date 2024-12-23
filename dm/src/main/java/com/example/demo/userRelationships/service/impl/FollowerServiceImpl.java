package com.example.demo.userRelationships.service.impl;

import com.example.demo.countService.countServiceServer.impl.CountServiceServerImpl;
import com.example.demo.userRelationships.dao.FollowerMapper;
import com.example.demo.userRelationships.dao.UserMapper;
import com.example.demo.userRelationships.entity.FollowingInfoResponse;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowerService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private FollowerMapper followerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CountServiceServerImpl countServiceServer;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserMapper userDao;



    @Override
    public ApiResponse<FollowingInfoResponse> getFollowerInfo(Long userId) {
        //如果用户的粉丝小于10000
        if (countServiceServer.getFollowerCount(userId) < 10000) {
            String followerKey = "follower_" + userId;
            //先查redis
            List<Long> followerList = redisUtils.zRevRange(followerKey);
            ///如果还是没有则去数据里面找
            if (followerList == null || followerList.size() == 0) {
                followerList = followerMapper.getFollowers(userId);
                List<Long> followerListUpdateTime = followerMapper.getFollowersUpdateTime(userId);
                if (followerList == null || followerList.size() == 0) {
                    //如果数据库里面也没有，那么就返回null
                    return ApiResponse.ok(null);
                } else {
                    //存到redis中去
                    int len = followerList.size();
                    for (int i = 0; i < len; i++) {
                        redisUtils.zAdd(followerKey, String.valueOf(followerList.get(i)),
                                followerListUpdateTime.get(i));
                    }
                }
            }
            List<User> users = userDao.findByUserIds(followerList);
            List<User> userResponses = users.stream().map(user -> {
                User userResponse = new User();
                userResponse.setCreatedAt(user.getCreatedAt());
                userResponse.setUpdatedAt(user.getUpdatedAt());
                userResponse.setUserId(Long.valueOf(user.getUserId().toString()));
                userResponse.setUserName(user.getUserName());
                userResponse.setNickName(user.getNickName());
                userResponse.setEmail(user.getEmail());
                userResponse.setAboutMe(user.getAboutMe());
                userResponse.setBirthday(LocalDateTime.parse(user.getBirthday().toString()));
                userResponse.setPhone(user.getPhone());
                userResponse.setRegion(user.getRegion());
                userResponse.setAvatar(user.getAvatar());
                return userResponse;
            }).collect(Collectors.toList());

            FollowingInfoResponse response = new FollowingInfoResponse();
            response.setCount(userResponses.size());
            response.setUserList(userResponses);
            return ApiResponse.ok(response);
        } else {
            //如果用户的粉丝大于10000
            //TODO: 做限流处理，然后直接从数据库中获取
            return ApiResponse.ok(null);
        }
    }

    @Override
    public FollowingInfoResponse getFollowingInfoServer(Long userId) {
        //如果用户的粉丝小于10000
        if (countServiceServer.getFollowerCount(userId) < 10000) {
            String followerKey = "follower_" + userId;
            //先查redis
            List<Long> followerList = redisUtils.zRevRange(followerKey);
            ///如果还是没有则去数据里面找
            if (followerList == null || followerList.size() == 0) {
                followerList = followerMapper.getFollowers(userId);
                List<Long> followerListUpdateTime = followerMapper.getFollowersUpdateTime(userId);
                if (followerList == null || followerList.size() == 0) {
                    //如果数据库里面也没有，那么就返回null
                    return null;
                } else {
                    //存到redis中去
                    int len = followerList.size();
                    for (int i = 0; i < len; i++) {
                        redisUtils.zAdd(followerKey, String.valueOf(followerList.get(i)),
                                followerListUpdateTime.get(i));
                    }
                }
            }
            List<User> users = userDao.findByUserIds(followerList);
            List<User> userResponses = users.stream().map(user -> {
                User userResponse = new User();
                userResponse.setCreatedAt(user.getCreatedAt());
                userResponse.setUpdatedAt(user.getUpdatedAt());
                userResponse.setUserId(Long.valueOf(user.getUserId().toString()));
                userResponse.setUserName(user.getUserName());
                userResponse.setNickName(user.getNickName());
                userResponse.setEmail(user.getEmail());
                userResponse.setAboutMe(user.getAboutMe());
                userResponse.setBirthday(LocalDateTime.parse(user.getBirthday().toString()));
                userResponse.setPhone(user.getPhone());
                userResponse.setRegion(user.getRegion());
                userResponse.setAvatar(user.getAvatar());
                return userResponse;
            }).collect(Collectors.toList());

            FollowingInfoResponse response = new FollowingInfoResponse();
            response.setCount(userResponses.size());
            response.setUserList(userResponses);
            return response;
        } else {
            //如果用户的粉丝大于10000
            //TODO: 做限流处理，然后直接从数据库中获取
            return null;
        }
    }
}
