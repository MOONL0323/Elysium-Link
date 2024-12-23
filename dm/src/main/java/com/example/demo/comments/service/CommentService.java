package com.example.demo.comments.service;

import com.example.demo.comments.entity.*;
import com.example.demo.util.ApiResponse;

import java.util.List;

/**
 * Original: 评论服务接口
 * @author 86188
 *
 * Change: Comment service interface
 * @auther MOONL
 */


public interface CommentService {
    void createComment(long userId, CommentVo commentVo, String content);

    void deleteComment(long commentId);

    ApiResponse<List<CommentResponse>> getLevelOneComment(long contentId, int page, int size);

    ApiResponse<List<CommentResponse>> getLevelTwoComment(long rootId, int page, int size);

    ApiResponse<List<CommentResponse>> getHistoryComment(long userId, int page, int size);
}
