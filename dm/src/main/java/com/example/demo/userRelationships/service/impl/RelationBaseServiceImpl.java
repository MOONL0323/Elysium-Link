package com.example.demo.userRelationships.service.impl;

import com.example.demo.userRelationships.dao.RelationBaseMapper;
import com.example.demo.userRelationships.service.RelationBaseService;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

/**
 * @Author MOONL
 * @version 2024/11/21
 *
 *  用户关系的基本信息service：关注人数、粉丝人数
 */

@Service
public class RelationBaseServiceImpl implements RelationBaseService {

    @Autowired
    private RelationBaseMapper relationBaseMapper;

    @Override
    public ApiResponse<String> getRelationCount(Long userId) {
        ApiResponse<String> response = new ApiResponse<>();
        Integer fansCount = relationBaseMapper.getFansCount(userId);
        Integer followingCount = relationBaseMapper.getFollowingCount(userId);
        response.setCode(200);
        response.setMessage("get relation count success");
        response.setData("user_id"+userId+"fansCount: " + fansCount + " followingCount: " + followingCount);
        return response;
    }
}
