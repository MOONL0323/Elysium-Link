package com.example.demo.userRelationships.dao;

import com.example.demo.userRelationships.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
}
