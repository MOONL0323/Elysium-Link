package com.example.demo.messagepush.consumers;

import com.example.demo.userRelationships.entity.FollowingInfoResponse;
import com.example.demo.userRelationships.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.demo.userRelationships.service.FollowerService;

import java.util.List;

/**
 * 用于监听kafla消息，创建Timeline执行器实例
 */

@Component
public class TimeLineSubExecuteBuilder {

    @Autowired
    private FollowerService followerService;

    @KafkaListener(topics = "event_timeline_push")
    public void execute(String message) {
        //"{creator_id:"+creatorIdStr+",item_id:"+item_id+",update_time:"+update_time+",batch:"+i+"}"
        //解析message，获取任务编号，创建Timeline执行器实例
        String taskIdStr = message.substring(message.lastIndexOf("task_id:") + 8);
        int taskId = Integer.parseInt(taskIdStr);
        //获取creator_id
        String creatorId = message.substring(message.lastIndexOf("creator_id:") + 11, message.lastIndexOf("item_id:") - 1);
        //获取item_id
        String itemId = message.substring(message.lastIndexOf("item_id:") + 8, message.lastIndexOf("update_time:") - 1);
        //获取该用户的所有粉丝
        FollowingInfoResponse followingInfoResponse = followerService.getFollowingInfoServer(Long.parseLong(creatorId));
        List<User> followingList = followingInfoResponse.getUserList();
        //获取所有粉丝的id
        List<Long> followingIdList = followingList.stream().map(User::getUserId).toList();
        int followerCount = followingInfoResponse.getCount();
        //截取对应的粉丝id列表，分成比如1-1000，1001-2000，2001-3000，3001-4000...
        int batch = followerCount / 1000;
        int remainder = followerCount % 1000;
        List<Long> subFollowingIdList;
        if(taskId <= batch) {
            subFollowingIdList = followingIdList.subList((taskId - 1) * 1000, 1000 * taskId);
        } else {
            subFollowingIdList = followingIdList.subList((taskId - 1) * 1000, 1000 * (taskId-1) + remainder);
        }
        //"{item_id:"+item_id+",update_time:"+update_time+",batch:"+i+"}"
        String updateTime = message.substring(message.lastIndexOf("update_time:") + 12, message.lastIndexOf("batch:") - 1);
        String msg = "{creator_id:"+creatorId+",item_id:"+itemId+",update_time:"+updateTime+"}";
        //交给对应的Timeline执行器处理
        TimeLineSubExecute.build(taskId).execute(subFollowingIdList, msg);
    }
}
