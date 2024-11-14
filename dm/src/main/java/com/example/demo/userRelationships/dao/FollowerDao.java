package com.example.demo.userRelationships.dao;

import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowerDao {
    List<Long> getFollower(@Param("userId") Long userId);

    void addFollower(@Param("userId") Long userId,
                     @Param("followerId") Long followerId);

    void deleteFollower(@Param("userId") Long userId,
                        @Param("followerId") Long followerId);
}
