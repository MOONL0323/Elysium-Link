//package com.example.demo.userRelationships.dao;
//
//import org.apache.ibatis.annotations.Mapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//
//
//public class FollowingRedisMapper {
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    /**
//     * 添加关注到关注列表Zset
//     * @param fromUserId
//     * @param toUserId
//     */
//    public void addFollowing(Long fromUserId, Long toUserId, Long updateTime) {
//        String followingKey = "following_" + fromUserId;
//        redisTemplate.opsForZSet().add(followingKey,
//                String.valueOf(toUserId), updateTime);
//    }
//
//    /**
//     * 添加关注到粉丝列表Zset
//     * @param fromUserId
//     * @param toUserId
//     * @param updateTime
//     */
//    public void addFollower(Long fromUserId, Long toUserId, Long updateTime) {
//        String followerKey = "follower_" + toUserId;
//        redisTemplate.opsForZSet().add(followerKey,
//                String.valueOf(fromUserId), updateTime);
//    }
//
//}
