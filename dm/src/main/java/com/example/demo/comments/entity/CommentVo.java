package com.example.demo.comments.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class CommentVo {
    /**
     * id
     */
    private Long cid;
    /**
     * 评论的文章信息id
     */
    private Long mid;
    /**
     * 发布评论的用户id
     */
    private Long uid;
    /**
     * 发布评论的用户名称&头像
     */
    private String userName;
    private String userAvatar;
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
     * 点赞次数&是否点赞
     */
    private Long count;
    private Boolean isAgree;
    /**
     * 二级评论数量
     */
    private Long twoNums;
    /**
     *所有子评论
     */
    private List<CommentVo> childrenComments;
    /**
     * 回复某一条评论的id
     */
    private Long replyId;

    /**
     * 回复某一条评论的用户id
     */
    private Long replyUid;
    private String replyName;
    private String replyContent;
}
