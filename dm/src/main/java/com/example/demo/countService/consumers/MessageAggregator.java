package com.example.demo.countService.consumers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageAggregator {

    private final LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final int batchSize;
    private final long waitTime;

    public MessageAggregator(int batchSize, long waitTime) {
        this.batchSize = batchSize;
        this.waitTime = waitTime;
    }

    public void addMessage(String message) {
        messageQueue.offer(message);
    }

    public List<String> getAggregatedMessages() throws InterruptedException {
        List<String> messages = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        while (messages.size() < batchSize && (System.currentTimeMillis() - startTime) < waitTime) {
            String message = messageQueue.poll(waitTime, TimeUnit.MILLISECONDS);
            if (message != null) {
                messages.add(message);
            }
        }

        return aggregateMessages(messages);
    }

    private List<String> aggregateMessages(List<String> messages) {
        Map<String, Integer> aggregatedMap = new HashMap<>();

        for (String message : messages) {
            String[] parts = message.split(" ");
            String key = parts[0] + " " + parts[1];
            int increment = Integer.parseInt(parts[2]);

            aggregatedMap.put(key, aggregatedMap.getOrDefault(key, 0) + increment);
        }

        List<String> aggregatedMessages = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : aggregatedMap.entrySet()) {
            aggregatedMessages.add(entry.getKey() + " " + entry.getValue());
        }

        return aggregatedMessages;
    }
}