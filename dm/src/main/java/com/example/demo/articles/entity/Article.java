package com.example.demo.articles.entity;

import lombok.Data;

@Data
public class Article {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long authorId;
    private String createTime;
    private String updateTime;
    /**
     * 点赞数
     */
    private Integer likeCount;
    /**
     * 评论数
     */
    private Integer commentCount;
}
