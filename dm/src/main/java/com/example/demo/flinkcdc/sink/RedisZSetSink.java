package com.example.demo.flinkcdc.sink;

import com.esotericsoftware.minlog.Log;
import com.example.demo.countService.countServiceServer.impl.CountServiceServerImpl;
import com.example.demo.util.RedisUtils;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

/**
 * RedisZSetSink
 * 伪从技术只应用在粉丝列表当中，因为关注列表不需要频繁变化所以采用先更新数据库然后删除缓存的方式
 * 但是粉丝列表需要频繁变化，所以采用伪从技术时刻更新缓存
 * 通过kafka监听binlog，然后更新缓存
 */

@Component
public class RedisZSetSink {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CountServiceServerImpl countServiceServer;

    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "audit-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        try (consumer) {
            consumer.subscribe(Collections.singletonList("event_binlog_mysqlToRedis"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    //kafka里面的数据格式如下：
                    //key:{"kafka_key":"from_user_id:111,to_user_id:111"}
                    //value:{"kafka_key":"from_user_id:111,to_user_id:111","id":111,"from_user_id":111,"to_user_id":111,"type":11,"update_time":111}
                    //如果是删除操作的话，key还是和上面一样，但是value是null
                    System.out.printf("Received message: %s%n", record.value());
                    String kafkaKey = record.key();
                    String value = record.value();
                    System.out.println("key: " + kafkaKey + ", message: " + value);
                    String toUserId = kafkaKey.split(",")[1].split(":")[1];
                    String fromUserId = kafkaKey.split(",")[0].split(":")[1];
                    String followerkey = "follower:" + toUserId;
                    //接下来就是按照增删改查来做分别处理
                    //由于我们mysql的following表采用的软删除，所以如果该行数据给删了说明是后台管理员操作，我们需要删除缓存来保持一致
                    if (value == null) {
                        //following进行了删除操作
                        //不需要删除整个粉丝列表，删除对应的关注者就好了。
                        redisUtils.zRemove(followerkey, fromUserId);
                        //因为我们采用的是软删除也就是说前端用户其实是做不到去删除数据库的行数据的，所以这里的关注者列表也需要删除
                        redisUtils.delete("following:" + fromUserId);
                        //计数服务也需要减少关注数和粉丝数
                        countServiceServer.decrUserInfo(Long.valueOf(fromUserId), "followingCount");
                        countServiceServer.decrUserInfo(Long.valueOf(toUserId), "followerCount");

                    } else {
                        //处理非空情况，也就是新增和更新的操作
                        //正常的业务情况来说，我们不允许后台管理人员直接对数据库表的关注者或者被关注者id的随意修改
                        //只允许增加或者修改type值做到对应的软删除或者软增加，此时变化的只有更新时间
                        //新增的情况和修改的情况从数据角度看是看不出来的，所以统一处理
                        String type = value.split(",")[4].split(":")[1];
                        long updateTime = Long.parseLong(value.split(",")[5].split(":")[1]);
                        if ("1".equals(type)) {
                            //新增或者修改
                            redisUtils.zAdd(followerkey, fromUserId, updateTime);
                            // 检查粉丝数量是否超过10000
                            int followerCount = countServiceServer.getFollowerCount(Long.valueOf(toUserId));
                            if (followerCount > 10000) {
                                // 删除时间最旧的粉丝数据
                                Set<String> oldFollowers = redisUtils.zRange(followerkey, 0, followerCount - 10001);
                                for (String oldFollower : oldFollowers) {
                                    redisUtils.zRemove(followerkey, oldFollower);
                                }
                            }
                        } else {
                            //删除
                            redisUtils.zRemove(followerkey, fromUserId);
                        }
                    }
                }
            }
        }
    }
}
