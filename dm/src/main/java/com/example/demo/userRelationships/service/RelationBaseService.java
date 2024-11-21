package com.example.demo.userRelationships.service;

import com.example.demo.util.ApiResponse;

public interface RelationBaseService {
    ApiResponse<String> getRelationCount(Long userId);
}
