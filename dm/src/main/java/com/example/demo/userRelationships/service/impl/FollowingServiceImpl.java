package com.example.demo.userRelationships.service.impl;

import com.example.demo.countService.countServiceServer.CountServiceServer;
import com.example.demo.countService.countServiceServer.impl.CountServiceServerImpl;
import com.example.demo.userRelationships.dao.FollowingMapper;
import com.example.demo.userRelationships.dao.UserMapper;
import com.example.demo.userRelationships.entity.FollowingInfoResponse;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowingService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.CaffeineUtil;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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
public class FollowingServiceImpl implements FollowingService {

    @Autowired
    private CountServiceServerImpl countServiceServer;

    @Autowired
    private CaffeineUtil<String,List<Long>> caffeineUtil;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FollowingMapper followingMapper;


    @Autowired
    private UserMapper userDao;

    @Override
    public List<Long> getFollowing(Long userId) {
        return followingMapper.getFollowing(userId);
    }


    /**
     * 关注
     * @param userId
     * @param followingId
     */
    @Override
    public void addFollowing(Long userId, Long followingId) {
        //因为我们哪怕取消了关注其实数据也是在的，只是type变了，所以这里不需要判断是否已经关注过，也就是软删除
        //显然我们可以使用一条sql语句去完成整个更新。
        long updateTime = System.currentTimeMillis();
        //直接更新following表的from_user_id,to_user_id,type和update_time，如果不存在则插入
        followingMapper.updateFollowingOrInsert(userId,followingId,1,updateTime);
        //TODO: 计数服务会因此增加关注数和粉丝数
        //关注列表并发量不高所以可以直接采用先更新数据库然后删除缓存的方式，并且方式是全量缓存、
        //直接删除缓存
        String followingKey = "following:" + userId;
        redisUtils.delete(followingKey);
        //Redis缓存,不能简单的创建加进去，因为可能会导致并发问题，先更新后删除也会有因为两阶段不是事务导致缓存过旧问题
        //采用订阅binlog+消息队列解决
        //缓存设计，关注列表和粉丝列表直接订阅的是following的变化，直接去负责分发的kafka中找即可，自动分发更新的这里我们可以打印出来看看结构
        //参考RedisZsetSink类
    }

    /**
     * 取消关注
     * @param userId
     * @param followingId
     */
    @Override
    public void deleteFollowing(Long userId, Long followingId) {
        List<Long> followingList = followingMapper.getFollowing(userId);
        //从业务的角度上看，前端应该做好校验，确保不会出现followingId不是userId关注的人的情况
        //采用软删除的策略
        followingMapper.deleteFollowing(userId, followingId);
        //粉丝列表缓存是使用伪从技术自动更新的，这里不做处理
        //关注列表缓存需要删除，因为是全量缓存，采用删除缓存的方式
        String followingKey = "following:" + userId;
        redisUtils.delete(followingKey);
    }

    /**
     * 获取关注列表
     * @param userId
     * @return ApiResponse<List<User>>
     */
    @Override
    public ApiResponse<FollowingInfoResponse> getFollowingInfo(Long userId) {
        String followingKey = "following_" + userId;
        //从caffeine中获取，如果没有则从redis中获取
        List<Long> followingList = caffeineUtil.get(followingKey);
        if (followingList == null) {
            followingList = redisUtils.zRevRange(followingKey);
        }
        //如果还是没有则去数据里面找
        if (followingList == null || followingList.size() == 0) {
            followingList = followingMapper.getFollowing(userId);
            List<Long> followingListUpdateTime = followingMapper.getFollowingUpdateTime(userId);
            if(followingList == null || followingList.size() == 0) {
                return ApiResponse.ok(null);
            }else{
                //存到redis中去
                int len = followingList.size();
                for (int i = 0; i < len; i++) {
                    redisUtils.zAdd(followingKey, String.valueOf(followingList.get(i)), followingListUpdateTime.get(i));
                }

                //判断是否需要存到caffeine中，按关注的时间排序，从近到远
                if(countServiceServer.getFollowerCount(userId) > 10000){
                    caffeineUtil.put(followingKey, followingList);
                }
            }
        }
        //不管redis中有无数据，封装数据返回
        List<User> users = userDao.findByUserIds(followingList);
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
    }

    /**
     * {
     *     "code": 0,
     *     "message": "success",
     *     "data": {
     *         "count": 2,
     *         "user_list": [
     *             {
     *                 "created_at": 1734520350,
     *                 "updated_at": 1734520350,
     *                 "user_id": "260001342867640320",
     *                 "user_name": "root2",
     *                 "nick_name": "",
     *                 "email": "root2@qq.com",
     *                 "about_me": "",
     *                 "birthday": "0001-01-01T00:00:00Z",
     *                 "phone": "",
     *                 "region": "",
     *                 "avatar": ""
     *             },
     *             {
     *                 "created_at": 1734520410,
     *                 "updated_at": 1734520410,
     *                 "user_id": "260001594437799936",
     *                 "user_name": "root3",
     *                 "nick_name": "",
     *                 "email": "root3@qq.com",
     *                 "about_me": "",
     *                 "birthday": "0001-01-01T00:00:00Z",
     *                 "phone": "",
     *                 "region": "",
     *                 "avatar": ""
     *             }
     *         ]
     *     },
     *     "error": ""
     * }
     */
}
