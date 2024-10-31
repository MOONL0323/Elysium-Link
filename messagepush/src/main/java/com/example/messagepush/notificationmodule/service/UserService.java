package com.example.messagepush.notificationmodule.service;

import com.example.messagepush.notificationmodule.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Text;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    

    //查询用户信息
    public String getUserInfo(String userId){
        return userMapper.getUserInfo(userId);
    }

    public String getFollowingList(String userid) {
        return userMapper.getFollowingList(userid);
    }

    public String getFollowersList(String userid) {
        return userMapper.getFollowersList(userid);
    }

    public Boolean followUser(String userid,String followingid) {
        return userMapper.followUser(userid,followingid);
    }

    public Boolean unfollowUser(String userid, String followingid) {
        return userMapper.unfollowUser(userid, followingid);
    }

    public String getTexts(String userid) {
        return userMapper.getTexts(userid);
    }

    public void writeNewText(String userid,String text) {
        userMapper.writeNewText(userid,text);
    }

    public String getFollowingTexts(String userid) {
        return userMapper.getFollowingTexts(userid);
    }
}
