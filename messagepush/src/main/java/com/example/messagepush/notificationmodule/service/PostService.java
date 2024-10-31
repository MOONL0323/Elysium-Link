// PostService.java
package com.example.messagepush.notificationmodule.service;

import com.example.messagepush.notificationmodule.mapper.PostMapper;
import com.example.messagepush.notificationmodule.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FollowService followService;

    @Autowired
    private NotificationService notificationService;

    public void createPost(Long userId, String content) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        postMapper.insertPost(post);

        notificationService.notifyFollowers(userId, content);
    }
}