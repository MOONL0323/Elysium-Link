package com.example.demo.messagepush.dao;

import com.example.demo.messagepush.entity.InboxInfoPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InboxMapper {

    /**
     * 插入收件箱
     * @param userId
     * @param contentId
     * @param publishTime
     * @return
     */
    @Insert("insert into inbox(user_id, content_id, create_time) values(#{userId}, #{contentId} #{publishTime})")
    int insertInbox(long userId, long contentId, long publishTime);

    /**
     * 读取收件箱前N条消息
     * @param userId
     * @param N
     */
    @Select("select content_id,public_time from inbox where user_id = #{userId} order by public_time desc limit #{N}")
    List<InboxInfoPo> getInboxNew(long userId, int N);

    /**
     * 读取收件箱下拉操作的历史消息
     * @param userId
     * @param pushTime
     * @param lastContentId
     * @param N
     */
    @Select("select content_id,push_time from inbox where user_id= #{userId} and push_time < #{pushTime} or (push_time = #{pushTime} and content_id < #{lastContentId}) order by push_time desc, content_id desc limit #{N}")
    List<InboxInfoPo> getInboxHistory(long userId, long pushTime, long lastContentId, int N);

}
