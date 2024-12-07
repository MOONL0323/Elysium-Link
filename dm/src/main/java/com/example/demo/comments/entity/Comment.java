package com.example.demo.comments.entity;

import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 86188
 */
@Data
@Table(name = "comment")
public class Comment implements Serializable {
    /**
     * 评论的id
     */
    private Long id;
    /**
     * 评论的文章信息id
     */
    private Long mid;
    /**
     * 发布评论的用户id
     */
    private Long uid;
    /**
     * 评论的父id
     */
    private Long pid;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 点赞次数
     */
    private Long count;
    /**
     * 二级评论数量
     */
    private Long twoNums;
    /**
     * 回复某一条评论的id
     */
    private Long replyId;

    /**
     * 回复某一条评论的用户id
     */
    private Long replyUid;
}
