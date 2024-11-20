package com.example.demo.comments.controller;

import com.example.demo.comments.entity.Comment;
import com.example.demo.comments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 86188
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.findById(id);
    }

    @GetMapping("/manuscript/{manuscriptId}")
    public List<Comment> getCommentsByManuscriptId(@PathVariable Long manuscriptId) {
        return commentService.findByManuscriptId(manuscriptId);
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) {
        commentService.insert(comment);
    }

    @PutMapping
    public void updateComment(@RequestBody Comment comment) {
        commentService.update(comment);
    }

    @DeleteMapping("/{id}/{creatorId}")
    public void deleteComment(@PathVariable Long id, @PathVariable Long creatorId) {
        commentService.delete(id, creatorId);
    }

    @PostMapping("/{id}/like")
    public void likeComment(@PathVariable Long id) {
        commentService.incrementLikeCount(id);
    }
}