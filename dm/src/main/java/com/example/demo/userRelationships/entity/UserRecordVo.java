package com.example.demo.userRelationships.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 86188
 */
@Data
public class UserRecordVo implements Serializable {
    private Long uid;
    /**
     * 新关注的用户数量
     */
    private Long addFollowCount;
    /**
     * 没有回复的数量
     */
    private Long noReplyCount;
    /**
     * 新点赞和收藏的数量
     */
    private Long agreeCollectionCount;

}
