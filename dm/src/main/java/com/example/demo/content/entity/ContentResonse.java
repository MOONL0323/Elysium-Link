package com.example.demo.content.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ContentResonse {
    private String content;
    private List<String> imageUris;
    private List<String> textUri;
    private Long videoId;
    private long updateTime;

}
