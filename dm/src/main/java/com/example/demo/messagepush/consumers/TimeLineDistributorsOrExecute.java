package com.example.demo.messagepush.consumers;

import com.example.demo.messagepush.service.MessagePushService;
import com.example.demo.userRelationships.entity.FollowingInfoResponse;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.service.FollowerService;
import com.example.demo.util.KafkaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Timeline分发器或者数据量小的情况下直接充当执行器
 * 订阅event_content_meta_change topic
 * @auther MOONL
 */
@Component
public class TimeLineDistributorsOrExecute {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private KafkaUtil kafkaUtil;

    /**
     * 监听event_content_meta_change topic，获取其中的消息进行处理
     * 消息格式就是文章的所有元数据，以及还有操作类型：c：新建发布，d:（用户）删除，r；（管理员）下架，u：更新
     * 举例的json格式如下：{item_id:1,creator_id:1,online_version:1,online_image_uris:[],online_video_id:1,
     * online_text_uri:111,latest_version:1,create_time:121,update_time:121,visibility:1,status:1,op:c}
     * @param message
     * @return
     */
    @KafkaListener(topics = "event_content_meta_change")
    public void distributeOrExecute(String message) {
        //获取消息中的操作类型
        String op = message.substring(message.lastIndexOf("op:") + 3);
        //根据操作类型进行不同的处理
        if("c".equals(op)) {
            //获取status
            String status = message.substring(message.lastIndexOf("status:") + 7, message.lastIndexOf("op:") - 1);
            //如果status为1，说明是发布状态，需要将文章推送到用户的timeline中
            if("1".equals(status)) {
                //获取visibility
                String visibility = message.substring(message.lastIndexOf("visibility:") + 11, message.lastIndexOf("status:") - 1);
                //如果是0，说明是私密的不公开的，如果是1或者2都是要向用户推送的
                if("1".equals(visibility) || "2".equals(visibility)) {
                    //推送到用户的timeline中
                    //获取creator_id
                    String creatorId = message.substring(message.lastIndexOf("creator_id:") + 11, message.lastIndexOf("online_version:") - 1);
                    //获取该用户的所有粉丝
                    FollowingInfoResponse followingInfoResponse = followerService.getFollowingInfoServer(Long.parseLong(creatorId));
                    List<User> followingList = followingInfoResponse.getUserList();
                    //获取所有粉丝的id
                    List<Long> followingIdList = followingList.stream().map(User::getUserId).toList();
                    int followerCount = followingInfoResponse.getCount();
                    //消息只有内容id和更新时间
                    String item_id = message.substring(message.lastIndexOf("item_id:") + 8, message.lastIndexOf("creator_id:") - 1);
                    String update_time = message.substring(message.lastIndexOf("update_time:") + 12, message.lastIndexOf("visibility:") - 1);
                    String creatorIdStr = message.substring(message.lastIndexOf("creator_id:") + 11, message.lastIndexOf("online_version:") - 1);
                    String msg= "{creator_id:"+creatorIdStr+",item_id:"+item_id+",update_time:"+update_time+"}";
                    //如果粉丝数量小于1000,直接充当执行器
                    if(followerCount < 1000) {
                        //直接推送
                        messagePushService.pushMessage(followingIdList, msg);
                    } else {
                        //否则充当分发器
                        //将粉丝列表分批，每批1000个，最后不足1000个的也算一批，每一批有个编号
                        int batch = followerCount / 1000;
                        int remainder = followerCount % 1000;
                        for(int i = 0; i < batch; i++) {
                            //将消息推送到消息队列中
                            kafkaUtil.sendMessage("event_content_push", "{creator_id:"+creatorIdStr+",item_id:"+item_id+",update_time:"+update_time+",taskId:"+i+"}");
                        }
                        if(remainder != 0) {
                            //将消息推送到消息队列中
                            kafkaUtil.sendMessage("event_content_push", "{creator_id:"+creatorIdStr+",item_id:"+item_id+",update_time:"+update_time+",taskId:"+batch+"}");
                        }
                    }
                }
            }
        }


    }




}
