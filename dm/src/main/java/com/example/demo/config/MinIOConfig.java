package com.example.demo.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data

public class MinIOConfig {
    /**
     * Minio 服务地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * Minio 服务端口号
     */
    @Value("${minio.port}")
    private Integer port;

    /**
     * Minio ACCESS_KEY
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * Minio SECRET_KEY
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * Minio 存储桶名称
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * Minio 文件上传路径
     */
    @Value("${minio.upload}")
    private String upload;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(endpoint, port, false)
                .credentials(accessKey, secretKey)
                .build();
    }

}