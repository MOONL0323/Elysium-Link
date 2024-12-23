package com.example.demo.content.service;

import com.example.demo.content.entity.ContentRequest;
import com.example.demo.content.entity.ContentResonse;
import com.example.demo.util.ApiResponse;

import java.util.List;

public interface ContentServer {
    void addContent(ContentRequest contentRequest);

    void updateContent(ContentRequest contentRequest, long itemId);

    void userDeleteContent(long itemId);

    void adminOfflineContent(long itemId);

    ApiResponse<List<Long>> getReader(long creatorId, int page, int size);

    ApiResponse<ContentResonse> getContent(long itemId);
}
