package com.example.demo.countService.controller;

import com.example.demo.countService.countServiceServer.CommentCoutServiceServer;
import com.example.demo.countService.countServiceServer.ContentCountServiceServer;
import com.example.demo.countService.entity.ContentInfoVo;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章相关计数服务Controller
 * @auther MOONL
 */
@RestController
@RequestMapping("/api/content")
public class ContentCountServiceController {

    @Autowired
    private ContentCountServiceServer countServiceServer;

    @Autowired
    private CommentCoutServiceServer commentCoutServiceServer;

    //*****************************************************************************

    /**
     * 获取某篇文章的文章id，点赞数，评论数，收藏数
     * @param articleId
     * @return
     */
    @GetMapping("/state")
    public ApiResponse<ContentInfoVo> getContentInfo(Long articleId) {
        return countServiceServer.getContentInfo(articleId);
    }

    //*****************************************************************************

    /**
     * 给该文章点赞
     */
    @PostMapping("/like")
    public void like(Long articleId) {
        countServiceServer.like(articleId);
    }

    /**
     * 给该文章取消点赞
     */
    @PostMapping("/unlike")
    public void unlike(Long articleId) {
        countServiceServer.unlike(articleId);
    }

    /**
     * 给该文章收藏
     */
    @PostMapping("/collect")
    public void collect(Long articleId) {
        countServiceServer.collect(articleId);
    }

    /**
     * 给该文章取消收藏
     */
    @PostMapping("/uncollect")
    public void uncollect(Long articleId) {
        countServiceServer.uncollect(articleId);
    }


    //*****************************************************************************

    /**
     * 给该评论点赞
     */
    @PostMapping("/likeComment")
    public void likeComment(Long commentId) {
        commentCoutServiceServer.likeComment(commentId);
    }

    /**
     * 给该评论取消点赞
     */
    @PostMapping("/unlikeComment")
    public void unlikeComment(Long commentId) {
        commentCoutServiceServer.unlikeComment(commentId);
    }


}
