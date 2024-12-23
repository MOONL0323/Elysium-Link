package com.example.demo.comments.entity;

import lombok.Data;

@Data
public class CommentDo {
    private long commentId;
    private long userId;
    private long replyCount;
    private long likeCount;
    private long commentTime;
}
