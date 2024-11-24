package com.example.demo.comments.entity;

import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Table(name = "comment")
public class Comment implements Serializable {

    // 随机生成
    private Long id;

    // 评论id
    private Long commentId;

    // 父评论id
    private Long parentId;

    // 根评论
    private Long rootId;

    // 评论的文章id, 来自于articles表
    private Long manuscriptId;

    // 评论内容
    private String content;

    // 评论者昵称
    private String creatorName;

    // 评论者id
    private Long creatorId;

    // 点赞数
    private Long likeCount;

    // 创建时间
    private Long creatorAt;

    // 文章作者id
    private Long authorId;
}
