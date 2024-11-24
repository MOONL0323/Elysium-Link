package com.example.demo.comments.service;


import com.example.demo.comments.entity.Comment;
import com.example.demo.comments.entity.CommentDTO;
import com.example.demo.util.ApiResponse;

import java.util.List;

/**
 * @author 86188
 */

public interface CommentService {
    Comment addComment(CommentDTO commentDTO);
    Comment getCommentById(Long id);
    void deleteComment(Long id, Long creatorId, Long authorId);
    void likeComment(Long id);
    ApiResponse<List<Comment>> getCommentsByManuscriptId(Long manuscriptId);
}
