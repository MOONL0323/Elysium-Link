package com.example.demo.userRelationships.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Original by：
 * @author 86188
 *
 * Change by:
 * @author MOONL
 *
 * 表结构说明：
 * fowllowers表：存储用户的粉丝列表
 * user_id: 用户id
 * follower_id: 粉丝id
 * eg: user_id = 1, follower_id = 2
 * 表示用户1的粉丝有用户2
 *
 */
@Mapper
public interface FollowerMapper {

    /**
     * 获取粉丝列表
     * @param userId
     * @return List<Long>
     */
    @Select("select follower_id from followers where user_id = #{userId}")
    List<Long> getFollower(@Param("userId") Long userId);

    /**
     * 添加粉丝，不对外提供接口，内部调用
     * @param userId
     * @param followerId
     */
    @Insert("insert into followers(user_id, follower_id) values(#{userId}, #{followerId})")
    void addFollower(@Param("userId") Long userId,
                     @Param("followerId") Long followerId);

    /**
     * 删除粉丝，不对外提供接口，内部调用
     * @param userId
     * @param followerId
     */
    @Delete("delete from followers where user_id = #{userId} and follower_id = #{followerId}")
    void deleteFollower(@Param("userId") Long userId,
                        @Param("followerId") Long followerId);
}
