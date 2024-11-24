package com.example.demo.comments.dao;

import com.example.demo.comments.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 86188
 */
@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (id, commentId, parentId, rootId, manuscriptId, content, creatorName, creatorId, likeCount, creatorAt, authorId) " +
            "VALUES (#{id}, #{commentId}, #{parentId}, #{rootId}, #{manuscriptId}, #{content}, #{creatorName}, #{creatorId}, #{likeCount}, #{creatorAt}, #{authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertComment(Comment comment);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment getCommentById(Long id);

    @Delete("DELETE FROM comment WHERE id = #{id} AND (creatorId = #{creatorId} OR authorId = #{authorId})")
    void deleteComment(@Param("id") Long id, @Param("creatorId") Long creatorId, @Param("authorId") Long authorId);

    @Update("UPDATE comment SET likeCount = likeCount + 1 WHERE id = #{id}")
    void likeComment(Long id);

    @Select("SELECT * FROM comment WHERE manuscriptId = #{manuscriptId}")
    List<Comment> getCommentsByManuscriptId(Long manuscriptId);

    @Delete("DELETE FROM comment WHERE rootId = #{rootId}")
    void deleteCommentsByRootId(Long rootId);
}