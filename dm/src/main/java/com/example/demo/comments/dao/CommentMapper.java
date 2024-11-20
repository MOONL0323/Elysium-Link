package com.example.demo.comments.dao;

import com.example.demo.comments.entity.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;

import java.util.List;


@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment findById(@Param("id") Long id);

    @Select("SELECT * FROM comment WHERE manuscriptId = #{manuscriptId}")
    List<Comment> findByManuscriptId(@Param("manuscriptId") Long manuscriptId);

    @Insert("INSERT INTO comment (commentId, parentId, rootId, manuscriptId, creatorAt, content, creatorId, likeCount) " +
            "VALUES (#{comment.commentId}, #{comment.parentId}, #{comment.rootId}, #{comment.manuscriptId}, " +
            "#{comment.creatorAt}, #{comment.content}, #{comment.creatorId}, #{comment.likeCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(@Param("comment") Comment comment);

    @Update("UPDATE comment SET content = #{comment.content}, likeCount = #{comment.likeCount}, creatorAt = #{comment.creatorAt} WHERE id = #{comment.id}")
    void update(@Param("comment") Comment comment);

    @Delete("DELETE FROM comment WHERE id = #{id} AND creatorId = #{creatorId}")
    void delete(@Param("id") Long id, @Param("creatorId") Long creatorId);

    @Update("UPDATE comment SET likeCount = likeCount + 1 WHERE id = #{id}")
    void incrementLikeCount(@Param("id") Long id);
}