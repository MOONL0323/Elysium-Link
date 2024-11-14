package com.example.demo.comments.entity;

import lombok.Data;

@Data
public class Comment {
    // 评论id
    private Long commentId;
    // 评论内容
    private String content;
    // 评论时间
    private String time;
    // 评论用户id
    private Long userId;
    // 评论用户昵称
    private String nickName;
    // 评论用户头像
    private String avatar;
    // 评论点赞数
    private Integer likeCount;
    // 评论回复数
    private Integer replyCount;
    // 评论是否被当前用户点赞
    private Boolean isCurrentUserLiked;
    // 评论是否被当前用户回复
    private Boolean isCurrentUserReplied;
}
