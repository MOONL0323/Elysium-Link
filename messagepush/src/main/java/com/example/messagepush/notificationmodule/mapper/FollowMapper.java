// FollowMapper.java
package com.example.messagepush.notificationmodule.mapper;

import com.example.messagepush.notificationmodule.model.Follow;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FollowMapper {

    @Insert("INSERT INTO follow_list(user_id, follow_id) VALUES(#{userId}, #{followId})")
    void insertFollow(Follow follow);

    @Delete("DELETE FROM follow_list WHERE user_id = #{userId} AND follow_id = #{followId}")
    void deleteFollow(Follow follow);

    @Select("SELECT follow_id FROM follow_list WHERE user_id = #{userId}")
    List<Long> findFollowsByUserId(Long userId);

    @Select("SELECT user_id FROM follow_list WHERE follow_id = #{userId}")
    List<Long> findFollowersByUserId(Long userId);

}