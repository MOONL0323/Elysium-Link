package com.example.demo.comments.controller;

import com.example.demo.comments.entity.CommentResponse;
import com.example.demo.comments.entity.CommentVo;
import com.example.demo.comments.service.CommentService;
import com.example.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 新建评论
     * @parm commentVo
     * @return
     */
    @PostMapping("/create")
    public void createComment(HttpServletRequest httpServletRequest,@RequestBody CommentVo commentVo,@RequestBody String content) {
        long userId = (long) httpServletRequest.getAttribute("userId");
        commentService.createComment(userId,commentVo,content);
        return;
    }

    /**
     * 删除评论
     * @parm commentId
     * @return
     */
    @PostMapping("/delete")
    public void deleteComment(HttpServletRequest httpServletRequest,@RequestBody long commentId) {
        long userId = (long) httpServletRequest.getAttribute("userId");
        commentService.deleteComment(commentId);
        return;
    }

    /**
     * 获取该内容下的一级评论
     * @parm contentId
     * @parm page
     * @parm size
     */
    @GetMapping("/getLevelOne")
    public ApiResponse<List<CommentResponse>> getLevelOneComment(@RequestParam long contentId, @RequestParam int page, @RequestParam int size) {
        return commentService.getLevelOneComment(contentId,page,size);
    }

    /**
     * 获取该评论下的二级评论
     * @parm rootId
     * @parm page
     * @parm size
     */
    @GetMapping("/getLevelTwo")
    public ApiResponse<List<CommentResponse>> getLevelTwoComment(@RequestParam long rootId, @RequestParam int page, @RequestParam int size) {
        return commentService.getLevelTwoComment(rootId,page,size);
    }

    /**
     * 查看用户自己之前发布过的历史评论
     * @parm userId
     * @parm page
     * @parm size
     */
    @GetMapping("/getHistory")
    public ApiResponse<List<CommentResponse>> getHistoryComment(HttpServletRequest httpServletRequest,@RequestParam int page, @RequestParam int size) {
        long userId = (long) httpServletRequest.getAttribute("userId");
        return commentService.getHistoryComment(userId,page,size);
    }


}