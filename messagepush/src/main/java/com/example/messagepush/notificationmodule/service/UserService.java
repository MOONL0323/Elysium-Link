// UserService.java
package com.example.messagepush.notificationmodule.service;

import com.example.messagepush.notificationmodule.mapper.UserMapper;
import com.example.messagepush.notificationmodule.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getAllUsers() {
        return userMapper.findAllUsers();
    }

    public User findUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void insertUser(User user){
        userMapper.insertUser(user);
    }

    public User findUserFollowByUsername(String username) {
        return userMapper.findUserFollowByUsername(username);
    }
}