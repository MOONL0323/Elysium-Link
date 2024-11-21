package com.example.demo.userRelationships.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author MOONL
 * @version 2024/11/21
 *
 * 关系基础信息Mapper：关注人数、粉丝人数dao
 */

@Mapper
public interface RelationBaseMapper {

    //获取粉丝人数
    @Select("select count(*) from follower where user_id = #{userId}")
    Integer getFansCount(Long userId);

    //获取关注人数
    @Select("select count(*) from following where user_id = #{userId}")
    Integer getFollowingCount(Long userId);
}
