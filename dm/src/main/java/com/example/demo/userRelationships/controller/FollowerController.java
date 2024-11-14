package com.example.demo.userRelationships.controller;

import com.example.demo.userRelationships.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 86188
 */
@RestController
@RequestMapping("/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @GetMapping("/{userId}")
    public List<Long> getFollower(@PathVariable Long userId) {
        return followerService.getFollower(userId);
    }

}
