package com.example.demo.comments.entity;

import com.example.demo.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "评论")
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论的文章信息id
     */
    @ApiModelProperty(value = "评论的文章信息id")
    @NotNull(message = "视频id不能为空", groups = DefaultGroup.class)
    private Long mid;
    /**
     * 发布评论的用户id
     */
    @ApiModelProperty(value = "发布评论的用户id")
    @NotNull(message = "用户id不能为空", groups = DefaultGroup.class)
    private Long uid;

    /**
     * 评论的父id
     */
    @ApiModelProperty(value = "评论的父id")
    private Long pid;

    /**
     * 回复某一条评论的id
     */
    @ApiModelProperty(value = "回复某一条评论的id")
    private Long replyId;

    @ApiModelProperty(value = "回复某一条评论的用户id")
    private Long replyUid;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "内容不能为空", groups = DefaultGroup.class)
    private String content;
}
