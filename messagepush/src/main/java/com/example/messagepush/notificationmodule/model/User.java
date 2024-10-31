// User.java
package com.example.messagepush.notificationmodule.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private List<User> follows;
}