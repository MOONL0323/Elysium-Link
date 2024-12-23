// FollowingInfoResponse.java
package com.example.demo.userRelationships.entity;

import lombok.Data;

import java.util.List;

@Data
public class FollowingInfoResponse {
        private int count;
        private List<User> userList;
}