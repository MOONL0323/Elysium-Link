package com.example.demo.userRelationships.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author 86188
 */
@Data
@Table(name = "user")
public class User {
    private Long id;

    private Long userId;

    private String username;
}
