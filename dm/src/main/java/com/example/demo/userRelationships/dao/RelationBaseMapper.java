package com.example.demo.userRelationships.dao;

import com.example.demo.userRelationships.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    //更新用户信息
    @Update("UPDATE user SET username = #{username}," +
            "password = #{password}, email = #{email}," +
            "phone = #{phone}, avatar = #{avatar} WHERE id = #{id}")
    void updateUser(User user);
}
