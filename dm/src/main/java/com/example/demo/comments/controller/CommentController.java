package com.example.demo.comments.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.comments.entity.Comment;
import com.example.demo.comments.entity.CommentDTO;
import com.example.demo.comments.entity.CommentVo;
import com.example.demo.comments.service.CommentService;
import com.example.demo.group.DefaultGroup;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 新增一条评论
     * @param commentDTO
     * @return ApiResponse<?>
     */

    @RequestMapping("/addComment")
    public ApiResponse<?> addComment(@RequestBody CommentDTO commentDTO) {
        ValidatorUtils.validateEntity(commentDTO, DefaultGroup.class);
        Comment comment = commentService.addComment(commentDTO);
        return ApiResponse.ok(comment);
    }

    /**
     * 得到所有的一级评论并携带一个二级评论
     * @param page
     * @param limit
     * @param mid
     * @param uid
     * @return ApiResponse<?>
     */
    @RequestMapping("/getComment/{id}")
    public ApiResponse<?> getComment(@PathVariable int page, @PathVariable int limit, @PathVariable Long mid, @PathVariable Long uid) {
        IPage<CommentVo> comment = commentService.getAllComment(page, limit, mid, uid);
        return ApiResponse.ok(comment);
    }

    /**
     * 删除评论
     * @param id
     * @param creatorId
     * @param authorId
     * @return ApiResponse<?>
     */
    @RequestMapping("/deleteComment/{id}")
    public ApiResponse<?> deleteComment(@PathVariable Long id, Long creatorId, Long authorId) {
        commentService.deleteComment(id);
        return ApiResponse.ok(null);
    }

}