package com.example.demo.countService.countServiceServer;

import com.example.demo.countService.entity.CommentInfoVo;
import com.example.demo.util.ApiResponse;

public interface CommentCoutServiceServer {
    ApiResponse<CommentInfoVo> getCommentInfo(Long commentId);
    public boolean incrCommentInfo(Long commentId, String field);
    boolean decrCommentInfo(Long commentId, String field);
    public Integer getHeat(Integer likeCount, Integer commentCount);

    void likeComment(Long commentId);

    void unlikeComment(Long commentId);
}
