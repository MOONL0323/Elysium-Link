package com.example.demo.countService.countServiceServer.impl;

import com.example.demo.countService.countServiceServer.ContentCountServiceServer;
import com.example.demo.countService.entity.ContentInfoVo;
import com.example.demo.countService.entity.UserInfoVo;
import com.example.demo.util.KafkaUtil;
import com.example.demo.util.RedisUtils;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContentCountServiceServerImpl implements ContentCountServiceServer {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private KafkaUtil kafkaUtil;

    @Override
    public ApiResponse<ContentInfoVo> getContentInfo(Long articleId) {
        ContentInfoVo contentInfoVo = new ContentInfoVo();
        String hKey = "count_content" + articleId;

        Map<Object, Object> map= redisUtils.hGetAll(hKey);
        Integer likeCount = Integer.parseInt(map.get("likeCount").toString());
        Integer commentCount = Integer.parseInt(map.get("commentCount").toString());
        Integer collectCount = Integer.parseInt(map.get("collectCount").toString());
        Integer heat = Integer.parseInt(map.get("heat").toString());

        contentInfoVo.setArticleId(articleId);
        contentInfoVo.setLikeCount(likeCount);
        contentInfoVo.setCommentCount(commentCount);
        contentInfoVo.setCollectCount(collectCount);
        contentInfoVo.setHeat(heat);
        return ApiResponse.ok(contentInfoVo);
    }

    /**
     * 点赞/收藏/评论+1
     * 从业务角度上看，在用户信息中，只有可能点赞/收藏/评论数量会大量增加，也就是过热数据我们做聚合写
     * @param articleId
     * @param field
     * @return boolean
     */
    @Override
    public boolean incrContentInfo(Long articleId, String field) {
        String hKey = "count_content" + articleId;
        //直接将redis命令写入消息队列，异步处理，直接返回
        String redisCommand = "HINCRBY" + hKey + " " + field + " " + 1;
        kafkaUtil.sendMessage("event_aggregateWrite_count",redisCommand);
        return true;
    }

    /**
     * 点赞/收藏/评论-1
     *  从业务角度上看，在用户信息中，只有可能点赞/收藏/评论数量会大量增加，也就是过热数据我们做聚合写
     * @param articleId
     * @param field
     * @return boolean
     */
    @Override
    public boolean decrUserInfo(Long articleId, String field) {
        String hKey = "count_content" + articleId;
        //直接将redis命令写入消息队列，异步处理，直接返回
        String redisCommand = "HINCRBY" + hKey + " " + field + " " + -1;
        kafkaUtil.sendMessage("event_aggregateWrite_count", redisCommand);
        return true;
    }

    /**
     * 某个字段+n
     * 从业务角度上看,这个方法是不会用到的，所以设置为private，只供内部调用
     * @param articleId
     * @param field
     * @param n
     * @return boolean
     */
    private boolean incrUserInfo(Long articleId, String field, Integer n) {
        String hKey = "count_content" + articleId;
        return redisUtils.hIncr(hKey, field, n);
    }

    /**
     * 批量设置若干用户信息，比如可以设置用户的关注数，粉丝数，文章数,用户热度（部分参数可以省略）
     * @param articleId
     * @param fieldsAndValues 可变参数，依次为 field1, value1, field2, value2, ...
     * @return boolean
     */
    @Override
    public boolean setUserInfo(Long articleId, String... fieldsAndValues) {
        String hKey = "count_content" + articleId;
        return redisUtils.hMSet(hKey, fieldsAndValues);
    }


    /**
     * 用户热度计算算法，可以根据实际情况调整
     * @param likeCount
     * @param commentCount
     * @param collectCount
     * @return Integer
     */
    @Override
    public Integer contentHeat(Integer likeCount, Integer commentCount, Integer collectCount) {
        return likeCount * 3 + commentCount * 2 + collectCount;
    }

    @Override
    public void unlike(Long articleId) {
        decrUserInfo(articleId, "likeCount");
    }

    @Override
    public void like(Long articleId) {
        incrContentInfo(articleId, "likeCount");
    }

    @Override
    public void collect(Long articleId) {
        incrContentInfo(articleId, "collectCount");
    }

    @Override
    public void uncollect(Long articleId) {
        decrUserInfo(articleId, "collectCount");
    }

}
