package com.example.demo.countService.countServiceServer.impl;

import com.example.demo.countService.countServiceServer.CommentCoutServiceServer;
import com.example.demo.countService.entity.CommentInfoVo;
import com.example.demo.countService.entity.ContentInfoVo;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.KafkaUtil;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommentCoutServiceServerImpl implements CommentCoutServiceServer {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private KafkaUtil kafkaUtil;

    @Override
    public ApiResponse<CommentInfoVo> getCommentInfo(Long commentId) {
        CommentInfoVo commentInfoVo = new CommentInfoVo();
        String hKey = "count_content" + commentId;

        Map<Object, Object> map= redisUtils.hGetAll(hKey);
        Long likeCount = Long.parseLong(map.get("likeCount").toString());
        Long commentCount = Long.parseLong(map.get("commentCount").toString());
        Integer heat = Integer.parseInt(map.get("heat").toString());

        commentInfoVo.setCommentId(commentId);
        commentInfoVo.setLikeCount(likeCount);
        commentInfoVo.setCommentCount(commentCount);
        commentInfoVo.setHeat(heat);
        return ApiResponse.ok(commentInfoVo);
    }

    /**
     * 点赞/评论+1
     * 从业务角度上看，在用户信息中，只有可能点赞/评论数量会大量增加，也就是过热数据我们做聚合写
     * @param commentId
     * @param field
     * @return boolean
     */
    @Override
    public boolean incrCommentInfo(Long commentId, String field) {
        String hKey = "count_content" + commentId;
        //直接将redis命令写入消息队列，异步处理，直接返回
        String redisCommand = "HINCRBY" + hKey + " " + field + " " + 1;
        kafkaUtil.sendMessage("event_aggregateWrite_count",redisCommand);
        return true;
    }

    /**
     * 点赞/评论-1
     *  从业务角度上看，在用户信息中，只有可能点赞/评论数量会大量增加，也就是过热数据我们做聚合写
     * @param commentId
     * @param field
     * @return boolean
     */
    @Override
    public boolean decrCommentInfo(Long commentId, String field) {
        String hKey = "count_content" + commentId;
        //直接将redis命令写入消息队列，异步处理，直接返回
        String redisCommand = "HINCRBY" + hKey + " " + field + " " + -1;
        kafkaUtil.sendMessage("event_aggregateWrite_count", redisCommand);
        return true;
    }



    /**
     * 用户热度计算算法，可以根据实际情况调整
     * @param likeCount
     * @param commentCount
     * @return Integer
     */
    @Override
    public Integer getHeat(Integer likeCount, Integer commentCount) {
        return likeCount * 2 + commentCount * 1;
    }


    @Override
    public void likeComment(Long commentId) {
        incrCommentInfo(commentId, "likeCount");
    }

    @Override
    public void unlikeComment(Long commentId) {
        decrCommentInfo(commentId, "likeCount");
    }
}
