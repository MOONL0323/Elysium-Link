package com.example.demo.userRelationships.dao;

import com.example.demo.userRelationships.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author MOONL
 * @version 2024/11/21
 *
 * 关系基础信息Mapper：关注人数、粉丝人数dao
 */

@Mapper
public interface RelationBaseMapper {

    //获取粉丝人数

    @Select("select count(*) from followers where user_id = #{userId}")
    Integer getFansCount(Long userId);

    //获取关注人数

    @Select("select count(*) from following where user_id = #{userId}")
    Integer getFollowingCount(Long userId);

    /*
    获取用户信息
     */

    @Select("<script>" +
        "SELECT * FROM user WHERE id IN " +
        "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>")
    List<User> getUserInfo(List<Long> list);
}
