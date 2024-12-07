package com.example.demo.articles.entity;


import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "t_collect_article")
@Data
public class CollectArticle {
    /**
     * 收藏id
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
     * 收藏时间
     */
    private String collectTime;
}
