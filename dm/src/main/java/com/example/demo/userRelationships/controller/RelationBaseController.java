package com.example.demo.userRelationships.controller;

import com.example.demo.userRelationships.service.RelationBaseService;
import com.example.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @auther MOONL
 * @version 2024/11/21
 *
 * 关系基础信息Controller：关注人数、粉丝人数
 */
@RestController
@RequestMapping("/api/relation")
public class RelationBaseController {

    @Autowired
    private RelationBaseService relationBaseService;

    @GetMapping("/status")
    public ApiResponse<String> getRelationStatus(HttpServletRequest request){
        Long userId= (Long) request.getAttribute("userId");
        return relationBaseService.getRelationCount(userId);
    }
}
