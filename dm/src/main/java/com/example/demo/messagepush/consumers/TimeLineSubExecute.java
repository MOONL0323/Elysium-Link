package com.example.demo.messagepush.consumers;

import com.example.demo.messagepush.service.MessagePushService;
import com.example.demo.messagepush.service.impl.MessagePushServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Timeline执行器
 */

public class TimeLineSubExecute {

    //任务编号,是作为Timeline执行器的唯一标识
    private int taskId;
    private MessagePushService messagePushService;

    // 存储所有实例的Map
    private static final ConcurrentHashMap<Integer, TimeLineSubExecute> instances = new ConcurrentHashMap<>();

    // 私有构造函数，防止外部直接创建实例
    private TimeLineSubExecute(int taskId) {
        this.taskId = taskId;
    }

    public static TimeLineSubExecute build(int taskId) {
        // 如果实例已经存在，直接返回
        if (instances.containsKey(taskId)) {
            return instances.get(taskId);
        }
        // 如果实例不存在，创建新实例并存储到Map中
        TimeLineSubExecute instance = new TimeLineSubExecute(taskId);
        instances.put(taskId, instance);
        return instance;
    }

    public void execute(List<Long> followingIdList, String message) {
        // 执行任务
        new Thread(() -> {
            // 任务执行逻辑
            messagePushService = new MessagePushServiceImpl();
            messagePushService.pushMessage(followingIdList, message);
        }).start();
    }
}
