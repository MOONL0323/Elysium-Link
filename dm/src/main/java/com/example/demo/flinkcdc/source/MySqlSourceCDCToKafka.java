package com.example.demo.flinkcdc.source;

import jakarta.annotation.PostConstruct;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.stereotype.Component;

@Component
public class MySqlSourceCDCToKafka {

    public void init() {
        System.out.println("CDC to Kafka");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60000);
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        String cdcSourceDDL = "CREATE TABLE following (" +
                "  id BIGINT NOT NULL," +
                "  from_user_id BIGINT NOT NULL," +
                "  to_user_id BIGINT NOT NULL," +
                "  type INT NOT NULL," +
                "  update_time BIGINT NOT NULL," +
                "  PRIMARY KEY (id) NOT ENFORCED" +
                ") WITH (" +
                "  'connector' = 'mysql-cdc'," +
                "  'hostname' = 'localhost'," +
                "  'port' = '3306'," +
                "  'username' = 'root'," +
                "  'password' = 'root'," +
                "  'database-name' = 'go-backend'," +
                "  'table-name' = 'following'," +
                "  'scan.startup.mode' = 'earliest-offset'" +
                ")";
        tableEnv.executeSql(cdcSourceDDL);


        // content_comment表的CDC源DDL
        String cdcSourceDDLContentComment = "CREATE TABLE content_comment (" +
                "  id BIGINT NOT NULL," +
                "  content_id BIGINT NOT NULL," +
                "  comment_id BIGINT NOT NULL," +
                "  root_id BIGINT NOT NULL," +
                "  level INT NOT NULL," +
                "  reply_count BIGINT NULL," +
                "  like_count BIGINT NULL," +
                "  reply_user_id BIGINT NULL," +
                "  reply_comment_id BIGINT NULL," +
                "  comment_time BIGINT NULL," +
                "  user_id BIGINT NOT NULL," +
                "  PRIMARY KEY (id) NOT ENFORCED" +
                ") WITH (" +
                "  'connector' = 'mysql-cdc'," +
                "  'hostname' = 'localhost'," +
                "  'port' = '3306'," +
                "  'username' = 'root'," +
                "  'password' = 'root'," +
                "  'database-name' = 'go-backend'," +
                "  'table-name' = 'content_comment'," +
                "  'scan.startup.mode' = 'earliest-offset'" +
                ")";
        tableEnv.executeSql(cdcSourceDDLContentComment);

        // user_comment表的Sink DDL
        String sinkDDLUserComment = "CREATE TABLE user_comment (" +
                "  id BIGINT NOT NULL," +
                "  content_id BIGINT NOT NULL," +
                "  comment_id BIGINT NOT NULL," +
                "  root_id BIGINT NOT NULL," +
                "  level INT NOT NULL," +
                "  reply_count BIGINT NULL," +
                "  like_count BIGINT NULL," +
                "  reply_user_id BIGINT NULL," +
                "  reply_comment_id BIGINT NULL," +
                "  comment_time BIGINT NULL," +
                "  user_id BIGINT NOT NULL," +
                "  PRIMARY KEY (id) NOT ENFORCED" +
                ") WITH (" +
                "  'connector' = 'jdbc'," +
                "  'url' = 'jdbc:mysql://localhost:3306/go-backend'," +
                "  'table-name' = 'user_comment'," +
                "  'username' = 'root'," +
                "  'password' = 'root'" +
                ")";
        tableEnv.executeSql(sinkDDLUserComment);

        // 将content_comment表的数据直接更新到user_comment表
        String toUserCommentQuerySQL = "INSERT INTO user_comment " +
                "SELECT id, content_id, comment_id, root_id, level, reply_count, like_count, reply_user_id, reply_comment_id, comment_time, user_id " +
                "FROM content_comment";
        tableEnv.executeSql(toUserCommentQuerySQL);




        String kafkaSinkDDLRedis = "CREATE TABLE kafka_sink_redis (" +
                "  kafka_key STRING NOT NULL," +
                "  id BIGINT NOT NULL," +
                "  from_user_id BIGINT NOT NULL," +
                "  to_user_id BIGINT NOT NULL," +
                "  type INT NOT NULL," +
                "  update_time BIGINT NOT NULL," +
                "  PRIMARY KEY (kafka_key) NOT ENFORCED" +
                ") WITH (" +
                "  'connector' = 'upsert-kafka'," +
                "  'topic' = 'event_binlog_mysqlToRedis'," +
                "  'key.format' = 'json'," +
                "  'value.format' = 'json'," +
                //"  'key.fields-prefix' = 'kafka_key'," +
                "  'properties.bootstrap.servers' = 'localhost:9092'" +
                ")";
        tableEnv.executeSql(kafkaSinkDDLRedis);

        String kafkaSinkDDLMysql = "CREATE TABLE kafka_sink_mysql (" +
                "  kafka_key STRING NOT NULL," +
                "  id BIGINT NOT NULL," +
                "  from_user_id BIGINT NOT NULL," +
                "  to_user_id BIGINT NOT NULL," +
                "  type INT NOT NULL," +
                "  update_time BIGINT NOT NULL," +
                "  PRIMARY KEY (kafka_key) NOT ENFORCED" +
                ") WITH (" +
                "  'connector' = 'upsert-kafka'," +
                "  'topic' = 'event_binlog_mysqlToMysql'," +
                "  'key.format' = 'json'," +
                "  'value.format' = 'json'," +
               //"  'key.fields-prefix' = 'kafka_key'," +
                "  'properties.bootstrap.servers' = 'localhost:9092'" +
                ")";
        tableEnv.executeSql(kafkaSinkDDLMysql);

        String toMysqlQuerySQL = "SELECT " +
                "  CONCAT('from_user_id:', CAST(from_user_id AS STRING), ',to_user_id:', CAST(to_user_id AS STRING)) AS kafka_key, " +
                "  id, from_user_id, to_user_id, type, update_time " +
                "FROM following";
        Table resultToSQL = tableEnv.sqlQuery(toMysqlQuerySQL);
        resultToSQL.executeInsert("kafka_sink_mysql");

        String toRedisQuerySQL = "SELECT " +
                "  CONCAT('from_user_id:', CAST(from_user_id AS STRING), ',to_user_id:', CAST(to_user_id AS STRING)) AS kafka_key, " +
                "  id, from_user_id, to_user_id, type, update_time " +
                "FROM following";
        Table resultToRedis = tableEnv.sqlQuery(toRedisQuerySQL);
        resultToRedis.executeInsert("kafka_sink_redis");
    }
}