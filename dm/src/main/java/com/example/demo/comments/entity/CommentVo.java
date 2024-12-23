package com.example.demo.comments.entity;

import lombok.Data;

@Data
public class CommentVo {
    private long contentId;
    private long rootId;
    private int level;
    private long replyUserId;
    private long replyCommentId;
}
