package com.example.demo.messagepush.service;

import com.example.demo.content.entity.ContentResonse;
import com.example.demo.util.ApiResponse;

import java.util.List;

public interface TimelineMessageGetServer {
    /**
     * 获取最新推送消息
     * @return
     */
    ApiResponse<List<ContentResonse>> getNewResponse(long userId, int N);

    /**
     * 获取历史推送消息
     */
    ApiResponse<List<ContentResonse>> getHistoryResponse(long userId,long pushTime,long lastContentId,int N);
}
