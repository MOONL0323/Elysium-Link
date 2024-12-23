package com.example.demo.content.entity;

import lombok.Data;

@Data
public class ContentPo {
    private String content;
    private String imageUris;
    private String textUri;
    private long videoId;
    private long onlineVersion;
    private long updateTime;
}
