package com.example.demo.messagepush.service.impl;

import com.example.demo.countService.countServiceServer.CountServiceServer;
import com.example.demo.messagepush.dao.InboxMapper;
import com.example.demo.messagepush.service.MessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagePushServiceImpl implements MessagePushService {

    @Autowired
    private CountServiceServer countServiceServer;

    @Autowired
    private InboxMapper inboxMapper;

    @Override
    public void pushMessage(List<Long> userId, String message) {
        //"{creator_id:"+creatorId+",item_id:"+itemId+",update_time:"+updateTime+"}"
        long creatorId = Long.parseLong(message.substring(message.lastIndexOf("creator_id:") + 11, message.lastIndexOf("item_id:") - 1));
        long itemId = Long.parseLong(message.substring(message.lastIndexOf("item_id:") + 8, message.lastIndexOf("update_time:") - 1));
        long updateTime = Long.parseLong(message.substring(message.lastIndexOf("update_time:") + 12));
        int creatorFollowerCount = countServiceServer.getFollowerCount(creatorId);

        //判断该创造者是否是大V
        if(creatorFollowerCount < 10000) {
            //是普通用户，直接推送消息
            for (Long aLong : userId) {
                inboxMapper.insertInbox(aLong,itemId,updateTime);
            }
        }else{
            //TODO：判断是否为活跃用户
            //是活跃用户，直接推送消息
            for (Long aLong : userId) {
                inboxMapper.insertInbox(aLong,itemId,updateTime);
            }
            //不是活跃用户，不推送消息
        }


    }
}
