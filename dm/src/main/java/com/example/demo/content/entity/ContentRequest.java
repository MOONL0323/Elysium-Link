package com.example.demo.content.entity;

import lombok.Data;

@Data
public class ContentRequest {
    private long creatorId;
    private String content;//短文本存在redis中
    private String imageUris;
    private String textUri;
    private long videoId;
    private int visibility;
}
