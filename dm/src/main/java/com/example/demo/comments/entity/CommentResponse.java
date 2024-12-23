package com.example.demo.comments.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private String commentUserName;
    private String content;
    private long commentCreateTime;
    private long commentLikeCount;
    private long commentReplyCount;
}
