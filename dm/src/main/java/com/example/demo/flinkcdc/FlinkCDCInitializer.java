package com.example.demo.flinkcdc;

import com.example.demo.flinkcdc.sink.RedisZSetSink;
import com.example.demo.flinkcdc.source.MySqlSourceCDCToKafka;
import com.example.demo.flinkcdc.sink.MySQLTableFollowerSink;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class FlinkCDCInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // Initialize and start MySqlSourceCDCToKafka
        executorService.submit(() -> {
            MySqlSourceCDCToKafka mySqlSourceCDCToKafka = new MySqlSourceCDCToKafka();
            mySqlSourceCDCToKafka.init();
        });

        // Initialize and start MySQLTableFollowerSink
        executorService.submit(() -> {
            MySQLTableFollowerSink mySQLTableFollowerSink = new MySQLTableFollowerSink();
            mySQLTableFollowerSink.init();
        });

        // Initialize and start RedisZSetSink
        executorService.submit(() -> {
            RedisZSetSink redisZSetSink = new RedisZSetSink();
            redisZSetSink.init();
        });

        executorService.shutdown();
    }
}