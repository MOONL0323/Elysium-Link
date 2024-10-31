package com.example.messagepush;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.messagepush.notificationmodule.mapper")
public class MessagepushApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagepushApplication.class, args);
    }

}
