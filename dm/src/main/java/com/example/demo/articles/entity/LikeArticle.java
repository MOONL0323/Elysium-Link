package com.example.demo.articles.entity;


import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "t_like_article")
@Data
public class LikeArticle {
    /**
     * 点赞id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 点赞时间
     */
    private String likeTime;
}
