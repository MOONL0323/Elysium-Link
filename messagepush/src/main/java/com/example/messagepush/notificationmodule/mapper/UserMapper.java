package com.example.messagepush.notificationmodule.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

@Component
public class UserMapper {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //根据id从redis中查询用户信息
    public String getUserInfo(String userId){
        return redisTemplate.opsForValue().get(userId);
    }

    public String getFollowingList(String userid) {
        return redisTemplate.opsForList().range(userid+"following", 0, -1).toString();
    }

    public String getFollowersList(String userid) {
        return redisTemplate.opsForList().range(userid+"followers", 0, -1).toString();
    }

    public Boolean followUser(String userid,String followingid) {
        return redisTemplate.opsForList().rightPush(userid+"following", followingid) > 0 &&
                redisTemplate.opsForList().rightPush(followingid+"followers", userid) > 0;
    }

    public Boolean unfollowUser(String userid, String followingid) {
        return redisTemplate.opsForList().remove(userid+"following", 0, followingid) > 0 &&
                redisTemplate.opsForList().remove(followingid+"followers", 0, userid) > 0;
    }

    public String getTexts(String userid) {
        StringBuilder str = new StringBuilder();
        for (String text : redisTemplate.opsForList().range(userid+"texts", 0, -1)) {
            str.append(text).append("/n").append("************");
        }
        return str.toString();
    }

    public void writeNewText(String userid,String text) {
        redisTemplate.opsForList().rightPush(userid+"texts", text);
        //消息推送（写扩散）
        for (String followerid : redisTemplate.opsForList().range(userid+"followers", 0, -1)) {
            redisTemplate.opsForList().rightPush(followerid+"followeringtexts", text);
        }
    }

    public String getFollowingTexts(String userid) {
        StringBuilder str = new StringBuilder();
        for (String followingid : redisTemplate.opsForList().range(userid+"followingtexts", 0, -1)) {
            str.append(getTexts(followingid)).append("/n").append("************");
        }
        return str.toString();
    }
}
