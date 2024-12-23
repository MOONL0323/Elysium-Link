package com.example.demo.countService.entity;

import lombok.Data;

@Data
public class ContentInfoVo {
    private Long articleId;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer heat;
}
