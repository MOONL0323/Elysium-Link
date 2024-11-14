package com.example.demo.userRelationships.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author 86188
 */
@Entity
@Table(name = "user") // 假设表名为 users
@Data
public class User {
    @jakarta.persistence.Id// 假设主键为 userId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 假设主键为 userId
    private Long userId;

    private String username;
}
