package com.example.demo.content.entity;

import lombok.Data;

@Data
public class ItemInfo {
    private long itemId;
    private long creatorId;
    private int onlineVersion;
    private String onlineImageUris;
    private long onlineVideoId;
    private String onlineTextUri;
    private int latestVersion;
    private long createTime;
    private long updateTime;
    private int visibility;
    private int status;
    private String extra;
}
