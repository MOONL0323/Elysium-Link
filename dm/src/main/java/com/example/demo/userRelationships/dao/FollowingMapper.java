package com.example.demo.userRelationships.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FollowingMapper {

    /**
     * 添加关注,如果有记录了，直接将type改为1，没有则插入
     * @param fromUserId
     * @param toUserId
     * @param type
     * @param updateTime
     */
    @Insert("insert into following(from_user_id, to_user_id, type, update_time) values(#{fromUserId}, #{toUserId}, #{type}, #{updateTime}) " +
            "on duplicate key update type = 1")
    void updateFollowingOrInsert(@Param("fromUserId") Long fromUserId,
                                  @Param("toUserId") Long toUserId,
                                    @Param("type") Integer type,
                                  @Param("updateTime") Long updateTime);

    /**
     * 删除关注,软删除，直接将type改为0
     * @param fromUserId
     * @param toUserId
     */
    @Update("update following set type = 0 where from_user_id = #{fromUserId} and to_user_id = #{toUserId}")
    void deleteFollowing(@Param("fromUserId") Long fromUserId,
                         @Param("toUserId") Long toUserId);


    /**
     * 获取关注列表,按照时间从近到远排序
     * @param userId
     * @return List<Long>
     */
    @Select("select to_user_id from following where from_user_id = #{userId} and type = 1 order by update_time desc")
    List<Long> getFollowing(@Param("userId") Long userId);

    /**
     * 获取关注列表的更新时间
     * @param userId
     * @return List<Long>
     */
    @Select("select update_time from following where from_user_id = #{userId} and type = 1")
    List<Long> getFollowingUpdateTime(@Param("userId") Long userId);
}
