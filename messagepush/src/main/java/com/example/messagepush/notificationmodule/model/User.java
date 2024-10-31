package com.example.messagepush.notificationmodule.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import java.util.List;

/**
 * @author MOONL
 */
@Data
@Component
public class User {
    private String userId;
    //private String password;
    private List<User> followers;
    //private List<User> following;

    /**
     * 撰写的内容
     */
    private List<String> texts;

}
