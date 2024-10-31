// UserMapper.java
package com.example.messagepush.notificationmodule.mapper;

import com.example.messagepush.notificationmodule.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users")
    List<User> findAllUsers();

    @Insert("INSERT INTO users(username, password) VALUES(#{username}, #{password})")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM follow_list WHERE user_id = (SELECT id FROM users WHERE username = #{username})")
    User findUserFollowByUsername(String username);
}