// PostMapper.java
package com.example.messagepush.notificationmodule.mapper;

import com.example.messagepush.notificationmodule.model.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    @Insert("INSERT INTO posts(user_id, content) VALUES(#{userId}, #{content})")
    void insertPost(Post post);
}