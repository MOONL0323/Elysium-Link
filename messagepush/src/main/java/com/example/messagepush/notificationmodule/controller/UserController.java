package com.example.messagepush.notificationmodule.controller;

import com.example.messagepush.notificationmodule.model.User;
import com.example.messagepush.notificationmodule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.w3c.dom.Text;

@Controller("/user")
public class UserController {

    @Autowired
    UserService userService;

    //获取用户个人基本信息
    @GetMapping("/{userid}")
    String getUserInfo(@RequestBody String userid){
        return userService.getUserInfo(userid);
    }

    //获取用户关注列表
    @GetMapping("/{userid}/following")
    String getFollowingList(@RequestBody String userid){
        return userService.getFollowingList(userid);
    }

    //获取用户粉丝列表
    @GetMapping("/{userid}/followers")
    String getFollowersList(@RequestBody String userid){
        return userService.getFollowersList(userid);
    }

    //关注用户
    @PostMapping("/{userid}/following")
    Boolean followUser(@RequestBody String userid, @RequestBody String followingid){
        return userService.followUser(userid,followingid);
    }

    //取消关注用户
    @PostMapping("/{userid}/unfollowing")
    Boolean unfollowUser(@RequestBody String userid, @RequestBody String followingid){
        return userService.unfollowUser(userid,followingid);
    }

    //获取用户撰写的内容，按照时间顺序，每篇文章中间用“************”隔开,返回文章内容
    @GetMapping("/{userid}/texts")
    String getTexts(@RequestBody String userid){
        return userService.getTexts(userid);
    }

    //撰写新内容,触发消息推送
    @PostMapping("/{userid}/texts")
    void writeNewText(@RequestBody String usedid,@RequestBody String text){
        userService.writeNewText(usedid,text);
    }

    //获取用户关注的人的文章
    @GetMapping("/{userid}/followingtexts")
    String getFollowingTexts(@RequestBody String userid){
        return userService.getFollowingTexts(userid);
    }



}
