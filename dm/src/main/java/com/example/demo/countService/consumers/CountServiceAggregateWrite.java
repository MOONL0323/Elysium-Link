package com.example.demo.countService.consumers;

import com.example.demo.util.RedisUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountServiceAggregateWrite {

    @Autowired
    private RedisUtils redisUtils;

    private final MessageAggregator messageAggregator = new MessageAggregator(10, 5000);

    @PostConstruct
    public void startMessageProcessingThread() {
        Thread messageProcessingThread = new Thread(() -> {
            try {
                while (true) {
                    List<String> aggregatedMessages = messageAggregator.getAggregatedMessages();
                    for (String aggregatedMessage : aggregatedMessages) {
                        String[] parts = aggregatedMessage.split(" ");
                        String key = parts[0];
                        String field = parts[1];
                        String increment = parts[2];

                        redisUtils.executeLuaScript("aggregate_write.lua", key, field, increment);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        messageProcessingThread.setDaemon(true);
        messageProcessingThread.start();
    }

    @KafkaListener(topics = "event_aggregateWrite_count")
    public void incrFollowerCount(String message) {
        messageAggregator.addMessage(message);
    }
}