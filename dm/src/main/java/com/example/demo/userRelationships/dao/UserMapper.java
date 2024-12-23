package com.example.demo.userRelationships.dao;

import com.example.demo.userRelationships.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 86188
 */
@Repository
@Mapper
public interface UserMapper{
    @Select("select * from user where user_id = #{userId}")
    User findByUserId(@Param("userId") Long userId);

    @Select("select * from user where username = #{username}")
    User findByUsername(@Param("username") String username);

    /**
     * 根据用户id列表查询多个用户的所有消息并且按照时间从近到远排序
     * @param userIds，用户id列表List<Long>
     * @return List<User>
     */
    @Select("<script>" +
            "SELECT * FROM user WHERE user_id IN " +
            "<foreach item='id' collection='userIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<User> findByUserIds(@Param("userIds") List<Long> userIds);
}
