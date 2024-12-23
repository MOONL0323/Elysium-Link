package com.example.demo.flinkcdc.sink;

import com.example.demo.userRelationships.dao.FollowerMapper;
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

/**
 * 同步follower表保持和following表一致
 */
@Component
public class MySQLTableFollowerSink {

    @Autowired
    private FollowerMapper followerMapper;

    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "audit-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("event_binlog_mysqlToMysql"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    //kafka里面的数据格式如下：
                    //key:{"kafka_key":"from_user_id:111,to_user_id:111"}
                    //value:{"kafka_key":"from_user_id:111,to_user_id:111","id":111,"from_user_id":111,"to_user_id":111,
                    // "type":11,"update_time":111}
                    long fromUserId = Long.parseLong(record.key().split(",")[0].split(":")[1]);
                    long toUserId = Long.parseLong(record.key().split(",")[1].split(":")[1]);
                    //后台管理删除
                    if(record.value() == null) {
                        //删除
                        followerMapper.deleteFollower(fromUserId, toUserId);
                    }else{
                        //更新
                        int type = Integer.parseInt(record.value().split(",")[4].split(":")[1]);
                        long updateTime = Long.parseLong(record.value().split(",")[5].split(":")[1]);
                        followerMapper.updateFollowerOrInsert(fromUserId, toUserId, type, updateTime);
                    }
                }

            }
        } finally {
            consumer.close();
        }
    }
}
