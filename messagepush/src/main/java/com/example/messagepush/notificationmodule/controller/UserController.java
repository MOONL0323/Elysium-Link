package com.example.messagepush.notificationmodule.controller;

import com.example.messagepush.notificationmodule.model.Follow;
import com.example.messagepush.notificationmodule.model.User;
import com.example.messagepush.notificationmodule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.insertUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/follows")
    public List<User> getFollows(@PathVariable String username) {
        return userService.findUserFollowByUsername(username).getFollows();
    }

}