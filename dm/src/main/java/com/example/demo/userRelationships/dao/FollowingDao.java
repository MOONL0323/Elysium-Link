package com.example.demo.userRelationships.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FollowingDao {
    @Select("select following_id from following where user_id = #{userId}")
    List<Long> getFollowing(@Param("userId") Long userId);

    @Insert("insert into following(user_id, following_id) values(#{userId}, #{followingId})")
    void addFollowing(@Param("userId") Long userId,
                      @Param("followingId") Long followingId);

    @Delete("delete from following where user_id = #{userId} and following_id = #{followingId}")
    void deleteFollowing(@Param("userId") Long userId,
                         @Param("followingId") Long followingId);
}
