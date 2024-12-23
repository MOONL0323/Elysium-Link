package com.example.demo.comments.service.impl;

import com.example.demo.comments.dao.CommentMapper;
import com.example.demo.comments.entity.CommentDo;
import com.example.demo.comments.entity.CommentResponse;
import com.example.demo.comments.entity.CommentSelfDo;
import com.example.demo.comments.entity.CommentVo;
import com.example.demo.comments.service.CommentService;
import com.example.demo.userRelationships.dao.UserMapper;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.RedisUtils;
import com.sankuai.inf.leaf.service.SnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SnowflakeService snowflakeService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void createComment(long userId, CommentVo commentVo, String content) {
        //看是哪一级的评论，是一级评论还是二级评论
        int level = commentVo.getLevel();
        if(level == 1){
            //一级评论
            long rootId = commentVo.getContentId();
            long replyUserId = 0;
            long replyCommentId = 0;
            long commentTime = System.currentTimeMillis();
            //生成唯一的评论id
            long commentId = snowflakeService.getId("comment").getId();
            //将评论存入redis
            redisUtils.set("comment:" + commentId, content);
            commentMapper.insertComment(commentVo.getContentId(), commentId, userId, rootId, level,
                    0, 0, replyUserId, replyCommentId, commentTime);
        }else{
            //二级评论
            long rootId = commentVo.getRootId();
            long replyUserId = commentVo.getReplyUserId();
            long replyCommentId = commentVo.getReplyCommentId();
            long commentTime = System.currentTimeMillis();
            //生成唯一的评论id
            long commentId = snowflakeService.getId("comment").getId();
            //将评论存入redis
            redisUtils.set("comment:" + commentId, content);
            commentMapper.insertComment(commentVo.getContentId(), commentId, userId, rootId, level,
                    0, 0, replyUserId, replyCommentId, commentTime);
        }
    }

    @Override
    public void deleteComment(long commentId) {
        //删除redis中的评论
        redisUtils.delete("comment:" + commentId);
        //删除评论
        commentMapper.deleteComment(commentId);
    }

    @Override
    public ApiResponse<List<CommentResponse>> getLevelOneComment(long contentId, int page, int size) {
        return ApiResponse.ok(getCommentResponseList(contentId, page, size));
    }

    @Override
    public ApiResponse<List<CommentResponse>> getLevelTwoComment(long rootId, int page, int size) {
        return ApiResponse.ok(getLevelTwoCommentResponseList(rootId, page, size));
    }

    @Override
    public ApiResponse<List<CommentResponse>> getHistoryComment(long userId, int page, int size) {
        return ApiResponse.ok(getHistoryCommentResponseList(userId, page, size));
    }

    private List<CommentResponse> getCommentResponseList(long contentId, int page, int size) {
        List<CommentDo> commentDoList = commentMapper.getLevelOneComment(contentId, (page - 1) * size, size);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        //将每一个CommentDo转换为CommentResponse
        for (CommentDo commentDo : commentDoList) {
            //根据userId获取commentUserName
            String commentUserName = userMapper.findByUserId(commentDo.getUserId()).getUserName();
            //commentId获取content，从redis中获取
            String content = redisUtils.get("comment:" + commentDo.getCommentId());
            CommentResponse commentResponse = new CommentResponse(commentUserName,content,commentDo.getCommentTime(),
                    commentDo.getLikeCount(),commentDo.getReplyCount());
            commentResponseList.add(commentResponse);
        }
        return commentResponseList;
    }

    private List<CommentResponse> getLevelTwoCommentResponseList(long rootId, int page, int size) {
        List<CommentDo> commentDoList = commentMapper.getLevelTwoComment(rootId, (page - 1) * size, size);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        //将每一个CommentDo转换为CommentResponse
        for (CommentDo commentDo : commentDoList) {
            //根据userId获取commentUserName
            String commentUserName = userMapper.findByUserId(commentDo.getUserId()).getUserName();
            //commentId获取content，从redis中获取
            String content = redisUtils.get("comment:" + commentDo.getCommentId());
            CommentResponse commentResponse = new CommentResponse(commentUserName,content,commentDo.getCommentTime(),
                    commentDo.getLikeCount(),commentDo.getReplyCount());
            commentResponseList.add(commentResponse);
        }
        return commentResponseList;
    }

    private List<CommentResponse> getHistoryCommentResponseList(long userId, int page, int size) {
        List<CommentSelfDo> commentDoList = commentMapper.getHistoryComment(userId, (page - 1) * size, size);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        //将每一个CommentDo转换为CommentResponse
        for (CommentSelfDo commentSelfDo : commentDoList) {
            //根据userId获取commentUserName
            String commentUserName = userMapper.findByUserId(commentSelfDo.getReplyUserId()).getUserName();
            //commentId获取content，从redis中获取
            String content = redisUtils.get("comment:" + commentSelfDo.getCommentId());
            CommentResponse commentResponse = new CommentResponse(commentUserName,content,commentSelfDo.getCommentTime(),
                    commentSelfDo.getLikeCount(),commentSelfDo.getReplyCount());
            commentResponseList.add(commentResponse);
        }
        return commentResponseList;
    }
}