package com.example.demo.countService.countServiceServer;

import com.example.demo.countService.entity.ContentInfoVo;
import com.example.demo.util.ApiResponse;

public interface ContentCountServiceServer {
    ApiResponse<ContentInfoVo> getContentInfo(Long articleId);
    boolean incrContentInfo(Long articleId, String field);
    boolean decrUserInfo(Long articleId, String field);
    boolean setUserInfo(Long articleId, String... fieldsAndValues);
    Integer contentHeat(Integer likeCount, Integer commentCount, Integer collectCount);
    void unlike(Long articleId);
    void like(Long articleId);
    void collect(Long articleId);
    void uncollect(Long articleId);
}
