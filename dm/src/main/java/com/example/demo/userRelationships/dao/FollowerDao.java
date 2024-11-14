package com.example.demo.userRelationships.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 86188
 */
@Mapper
public interface FollowerDao {
    @Select("select follower_id from followers where user_id = #{userId}")
    List<Long> getFollower(@Param("userId") Long userId);

    @Insert("insert into followers(user_id, follower_id) values(#{userId}, #{followerId})")
    void addFollower(@Param("userId") Long userId,
                     @Param("followerId") Long followerId);

    @Delete("delete from followers where user_id = #{userId} and follower_id = #{followerId}")
    void deleteFollower(@Param("userId") Long userId,
                        @Param("followerId") Long followerId);
}
