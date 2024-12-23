//package com.example.demo.userRelationships.controller;
//
//import com.example.demo.userRelationships.entity.User;
//import com.example.demo.userRelationships.entity.UserRecordVo;
//import com.example.demo.userRelationships.service.RelationBaseService;
//import com.example.demo.util.ApiResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
///**
// * @author 86188
// * @auther MOONL
// * @version 2024/11/21
// *
// * 关系基础信息Controller：关注人数、粉丝人数
// */
//@RestController
//@RequestMapping("/api/relation")
//public class RelationBaseController {
//
//    @Autowired
//    private RelationBaseService relationBaseService;
//
//
//    /**
//     * 获取关系状态
//     * @param request
//     * @return
//     */
//    @GetMapping("/status")
//    public ApiResponse<String> getRelationStatus(HttpServletRequest request){
//        Long userId= (Long) request.getAttribute("userId");
//        return relationBaseService.getRelationCount(userId);
//    }
//
//    /**
//     * 更新用户信息
//     *
//     * @param user
//     * @return
//     */
//    @RequestMapping("updateUser")
//    public ApiResponse<?> updateUser(@RequestBody User user) {
//        User user1 = relationBaseService.updateUser(user);
//        return ApiResponse.ok(user1);
//    }
//
//    @RequestMapping("getUserRecord")
//    public ApiResponse<?> getUserRecord(String uid) {
//        UserRecordVo userRecordVo = relationBaseService.getUserRecord(uid);
//        return ApiResponse.ok(userRecordVo);
//    }
//}
