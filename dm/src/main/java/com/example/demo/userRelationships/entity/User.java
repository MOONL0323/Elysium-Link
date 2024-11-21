package com.example.demo.userRelationships.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86188
 */
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String userName;
    private String nickName;
    private String email;
    private String password;
    private String aboutMe;
    private LocalDateTime birthday;
    private String phone;
    private String region;
    private String avatar;
}
