package com.example.demo.comments.service.impl;

import com.example.demo.comments.dao.CommentMapper;
import com.example.demo.comments.entity.Comment;
import com.example.demo.comments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Comment findById(Long id) {
        return commentMapper.findById(id);
    }

    @Override
    public List<Comment> findByManuscriptId(Long manuscriptId) {
        return commentMapper.findByManuscriptId(manuscriptId);
    }

    @Override
    public void insert(Comment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void update(Comment comment) {
        commentMapper.update(comment);
    }

    @Override
    public void delete(Long id, Long creatorId) {
        commentMapper.delete(id, creatorId);
    }

    @Override
    public void incrementLikeCount(Long id) {
        commentMapper.incrementLikeCount(id);
    }
}
