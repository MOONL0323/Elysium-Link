// 发布功能
package com.example.messagepush.notificationmodule.model;

import lombok.Data;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String content;

    // getters and setters
}