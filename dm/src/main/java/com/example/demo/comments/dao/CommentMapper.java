package com.example.demo.comments.dao;


import com.example.demo.comments.entity.CommentDo;
import com.example.demo.comments.entity.CommentResponse;
import com.example.demo.comments.entity.CommentSelfDo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Original:
 * @author 86188
 *
 * Change:
 * @author MOONL
 */
@Mapper
public interface CommentMapper {

    /**
     * 新增评论
     * @parm contentId
     * @parm commentId
     * @parm userId
     * @parm rootId
     * @parm level
     * @parm replyCount
     * @parm likeCount
     * @parm replyUserId
     * @parm replyCommentId
     * @parm commentTime
     * @return
     */
    @Insert("insert into content_comment(content_id, comment_id, user_id, root_id, level, reply_count, like_count, reply_user_id" +
            ", reply_comment_id, comment_time) values(#{contentId}, #{commentId}, #{userId}, #{rootId}, #{level}," +
            " #{replyCount}, #{likeCount}, #{replyUserId}, #{replyCommentId}, #{commentTime})")
    int insertComment(long contentId, long commentId, long userId, long rootId, int level, int replyCount, int likeCount,
                      long replyUserId, long replyCommentId, long commentTime);

    /**
     * 删除评论
     * @param commentId
     */
    @Delete("delete from content_comment wherecomment_id = #{commentId}")
    void deleteComment(long commentId);

    /**
     * 获取一级评论
     * @param contentId
     * @param M
     * @param size
     * @return List<CommentResponse>
     */
    @Select("select comment_id,user_id,reply_count,like_count,comment_time from content_comment where root_id= #{contentId} and level = 1 order by comment_time desc limit #{M},#{size}")
    List<CommentDo> getLevelOneComment(long contentId, int M, int size);


    /**
     * 获取二级评论
     * @param rootId
     * @param N
     * @param size
     * @return List<CommentResponse>
     */
    @Select("select comment_id,user_id,reply_count,like_count,comment_time from content_comment where root_id= #{rootId} and level = 2 order by comment_time desc limit #{N},#{size}")
    List<CommentDo> getLevelTwoComment(long rootId, int N, int size);

    /**
     * 获取用户历史评论
     * @param userId
     * @param N
     * @param size
     * @return List<CommentResponse>
     */
    @Select("select comment_id,like_count,reply_count,comment_time,reply_user_id,reply_comment_id from user_comment where user_id = #{userId} order by comment_time desc limit #{N},#{size}")
    List<CommentSelfDo> getHistoryComment(long userId, int N, int size);
}