package com.example.demo.articles.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author 86188
 */
@Data
@Table(name = "t_article")
public class Article {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long authorId;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    private String articleUrl;
    /**
     * 点赞数
     */
    private Integer likeCount;
    /**
     * 评论数
     */
    private Integer commentCount;
    /**
     * 浏览数
     */
    private Integer viewCount;
    /**
     * 是否置顶
     */
    private Boolean isTop;
    /**
     * 收藏数
     */
    private Integer collectCount;
}
