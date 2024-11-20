package com.example.demo.comments.service;


import com.example.demo.comments.entity.Comment;

import java.util.List;

/**
 * @author 86188
 */
public interface CommentService {
    Comment findById(Long id);
    List<Comment> findByManuscriptId(Long manuscriptId);
    void insert(Comment comment);
    void update(Comment comment);
    void delete(Long id, Long creatorId);
    void incrementLikeCount(Long id);
}
