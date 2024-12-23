package com.example.demo.countService.countServiceServer.impl;

import com.example.demo.countService.countServiceServer.CountServiceServer;
import com.example.demo.countService.entity.ContentInfoVo;
import com.example.demo.countService.entity.UserInfoVo;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.KafkaUtil;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CountServiceServerImpl implements CountServiceServer {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private KafkaUtil kafkaUtil;

    /**
     * 获取用户维度的信息，用户id，关注数，粉丝数，文章数,用户热度
     * @param userId
     * @return ApiResponse<UserInfoVo>
     */
    @Override
    public ApiResponse<UserInfoVo> getUserInfo(Long userId) {
        UserInfoVo userInfoVo = new UserInfoVo();
        String hKey = "count_user" + userId;

        Map<Object, Object> map= redisUtils.hGetAll(hKey);
        Integer followingCount = Integer.parseInt(map.get("followingCount").toString());
        Integer followerCount = Integer.parseInt(map.get("followerCount").toString());
        Integer articleCount = Integer.parseInt(map.get("articleCount").toString());
        Integer userHeat = Integer.parseInt(map.get("userHeat").toString());

        userInfoVo.setUserId(userId);
        userInfoVo.setFollowingCount(followingCount);
        userInfoVo.setFollowerCount(followerCount);
        userInfoVo.setArticleCount(articleCount);
        userInfoVo.setUserHeat(userHeat);
        return ApiResponse.ok(userInfoVo);
    }

    /**
     * 某个字段+1
     * 从业务角度上看，在用户信息中，只有可能粉丝数量会大量增加，也就是过热数据我们做聚合写
     * @param userId
     * @param field
     * @return boolean
     */
    @Override
    public boolean incrUserInfo(Long userId, String field) {
        String hKey = "count_user" + userId;
        if("followerCount".equals(field)) {
            //直接将redis命令写入消息队列，异步处理，直接返回
            String redisCommand = "HINCRBY" + hKey + " " + field + " " + 1;
            kafkaUtil.sendMessage("event_aggregateWrite_count",redisCommand);
            return true;
        }
        return redisUtils.hIncr(hKey, field, 1);
    }

    /**
     * 某个字段-1
     * 从业务角度上看，在用户信息中，只有可能粉丝数量会大量增加，也就是过热数据我们做聚合写
     * @param userId
     * @param field
     * @return boolean
     */
    @Override
    public boolean decrUserInfo(Long userId, String field) {
        String hKey = "count_user" + userId;
        if ("followerCount".equals(field)) {
            //直接将redis命令写入消息队列，异步处理，直接返回
            String redisCommand = "HINCRBY" + hKey + " " + field + " " + -1;
            kafkaUtil.sendMessage("event_aggregateWrite_count", redisCommand);
            return true;
        }
        return redisUtils.hIncr(hKey, field, -1);
    }

    /**
     * 某个字段+n
     * 从业务角度上看,这个方法是不会用到的，所以设置为private，只供内部调用
     * @param userId
     * @param field
     * @param n
     * @return boolean
     */
    private boolean incrUserInfo(Long userId, String field, Integer n) {
        String hKey = "count_user" + userId;
        return redisUtils.hIncr(hKey, field, n);
    }

    /**
     * 批量设置若干用户信息，比如可以设置用户的关注数，粉丝数，文章数,用户热度（部分参数可以省略）
     * @param userId
     * @param fieldsAndValues 可变参数，依次为 field1, value1, field2, value2, ...
     * @return boolean
     */
    @Override
    public boolean setUserInfo(Long userId, String... fieldsAndValues) {
        String hKey = "count_user" + userId;
        return redisUtils.hMSet(hKey, fieldsAndValues);
    }

    /**
     * 设置所有用户信息
     * @param userId
     * @param userInfoVo
     * @return boolean
     */
    @Override
    public boolean setUserInfo(Long userId, UserInfoVo userInfoVo) {
        String hKey = "count_user" + userId;
        return redisUtils.hMSet(hKey, userInfoVo);
    }

    /**
     * 用户热度计算算法，可以根据实际情况调整
     * @param followerCount
     * @param articleCount
     * @return Integer
     */
    @Override
    public Integer userHeat(Integer followerCount, Integer articleCount) {
        return followerCount * 3 + articleCount * 2;
    }

    /**
     * 获取粉丝数量
     * @param userId
     * @return Integer
     */
    @Override
    public Integer getFollowerCount(Long userId) {
        String hKey = "count_user" + userId;
        return redisUtils.hGet(hKey, "followerCount");
    }

}
