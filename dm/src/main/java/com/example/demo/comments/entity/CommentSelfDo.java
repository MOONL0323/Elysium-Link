package com.example.demo.comments.entity;

import lombok.Data;

@Data
public class CommentSelfDo {
    //comment_id,like_count,comment_time,reply_user_id,reply_comment_id
    private long commentId;
    private long likeCount;
    private long replyCount;
    private long commentTime;
    private long replyUserId;
    private long replyCommentId;
}
