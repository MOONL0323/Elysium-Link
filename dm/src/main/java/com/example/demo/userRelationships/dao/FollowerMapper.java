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
 * fowllowers表与fowllowing表保存数据一致
 * 提供的CUD接口仅供同步使用
 * 查询接口供外部使用
 *
 */
@Mapper
public interface FollowerMapper {


    /**
     * 增加数据，不对外提供接口，内部调用
     */
    @Insert("INSERT INTO follower (from_user_id, to_user_id, type, update_time) VALUES (#{fromUserId}, #{toUserId}, #{type}, #{updateTime})")
    void addFollower(@Param("fromUserId") Long fromUserId,
                     @Param("toUserId") Long toUserId,
                     @Param("type") Integer type,
                     @Param("updateTime") Long updateTime);

    /**
     * 删除数据，不对外提供接口，内部调用
     */
    @Delete("DELETE FROM follower WHERE from_user_id = #{fromUserId} AND to_user_id = #{toUserId}")
    void deleteFollower(@Param("fromUserId") Long fromUserId,
                        @Param("toUserId") Long toUserId);

    /**
     * 更新数据，如果不存在则插入，不对外提供接口，内部调用
     */
    @Insert("INSERT INTO follower (from_user_id, to_user_id, type, update_time) VALUES (#{fromUserId}, #{toUserId}, #{type}, #{updateTime}) " +
            "ON DUPLICATE KEY UPDATE type = #{type}, update_time = #{updateTime}")
    void updateFollowerOrInsert(@Param("fromUserId") Long fromUserId,
                                @Param("toUserId") Long toUserId,
                                @Param("type") Integer type,
                                @Param("updateTime") Long updateTime);

    /**
     * 获取粉丝列表,按照时间从近到远排序
     * @param userId
     * @return List<Long>
     */
    @Select("SELECT from_user_id FROM follower WHERE to_user_id = #{userId} AND type = 1 ORDER BY update_time DESC")
    List<Long> getFollowers(@Param("userId") Long userId);

    /**
     * 获取粉丝列表的更新时间
     * @param userId
     * @return List<Long>
     */
    @Select("SELECT update_time FROM follower WHERE to_user_id = #{userId} AND type = 1")
    List<Long> getFollowersUpdateTime(@Param("userId") Long userId);
}
