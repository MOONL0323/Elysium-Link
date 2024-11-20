package com.example.demo.comments.entity;

import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "comment")
public class Comment {
    private Long id;
    // 评论id
    private Long commentId;

    // 评论的父评论id
    private Long parentId;

    //根评论
    private Long rootId;

    // 评论的文章id,来自于manuscript表
    private Long manuscriptId;

    private Long creatorAt;

    // 评论内容
    private String content;

    // 评论者id
    private Long creatorId;
    //点赞数
    private Long likeCount;

}
