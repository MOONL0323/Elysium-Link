package com.example.demo.auditingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuditCenter {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "event_audit_content", groupId = "audit-group")
    public void listen(String message) {
        // 处理接收到的消息，格式是itemId+"_"+version
        System.out.println("Received message: " + message);
        // 审核逻辑
        String itemId = message.split("_")[0];
        String version = message.split("_")[1];
        //返回审核结果：item_id+"_"+version+"_"+result+"_"+reason
        if(true){
            message = message + "_1_";
        }else{
            message = message + "_2_"+ "0";
        }
        // 投送信息到 event_audit_result
        kafkaTemplate.send("event_audit_result", message);
    }
}