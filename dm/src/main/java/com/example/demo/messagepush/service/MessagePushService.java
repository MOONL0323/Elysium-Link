package com.example.demo.messagepush.service;

import java.util.List;

/**
 * 消息推送服务
 */
public interface MessagePushService {
    /**
     * 推送消息
     * @param userId
     * @param content
     */
    void pushMessage(List<Long> userId, String content);

}
