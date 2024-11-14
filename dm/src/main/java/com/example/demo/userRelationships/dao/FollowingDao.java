package com.example.demo.userRelationships.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface FollowingDao {
    List<Long> getFollowing(@Param("userId") Long userId);

    void addFollowing(@Param("userId") Long userId,
                      @Param("followingId") Long followingId);

    void deleteFollowing(@Param("userId") Long userId,
                         @Param("followingId") Long followingId);
}
