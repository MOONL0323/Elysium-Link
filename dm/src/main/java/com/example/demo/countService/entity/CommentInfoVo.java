package com.example.demo.countService.entity;


import lombok.Data;
import org.springframework.data.relational.core.sql.In;

@Data
public class CommentInfoVo {
    private Long commentId;
    private Long likeCount;
    private Long commentCount;
    private Integer heat;
}
