package com.example.demo.comments.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.comments.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @author 86188
 */

public interface CommentService extends IService<Comment> {
    /**
     * 得到所有的一级评论并携带一个二级评论
     *
     * @param page
     * @param limit
     * @param mid
     * @param uid
     * @return
     */
    IPage<CommentVo> getAllComment(Long page, Long limit, Long mid, Long uid);

    /**
     * 增加一条评论
     * @param commentDTO
     * @return
     */
    Comment addComment(CommentDTO commentDTO);

    /**
     * 得到所有的二级评论根据一级评论id
     *
     * @param page
     * @param limit
     * @param id
     * @param uid
     * @return
     */
    IPage<Comment> getAllTwoCommentByOneId(Long page, Long limit, Long id, Long uid);

    /**
     * 查看所有回复的评论
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    List<Comment> getAllReplyComment(Long page, Long limit, Long uid);

    /**
     * 删除一条评论
     * @param id
     */
    void deleteComment(Long id);

    /**
     * 跳转到评论详情
     * @param id
     * @param mid
     * @param uid
     * @return
     */
    Map<String, Object> scrollComment(Long id, Long mid, Long uid);
}
