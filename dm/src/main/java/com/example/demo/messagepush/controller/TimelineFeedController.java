package com.example.demo.messagepush.controller;

import com.example.demo.content.entity.ContentResonse;
import com.example.demo.messagepush.service.TimelineMessageGetServer;
import com.example.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户执行上滑获取最新推送消息/下拉获取历史推送消息
 */
@RestController
@RequestMapping("/api/messagePush/timelineFeed")
public class TimelineFeedController {

    @Autowired
    private TimelineMessageGetServer timelineMessageGetServer;

    @PostMapping("/getNew")
    public ApiResponse<List<ContentResonse>> getNew(HttpServletRequest request, @RequestBody int N) {
        long userId = (long) request.getAttribute("userId");
        // 获取最新推送消息
        return timelineMessageGetServer.getNewResponse(userId, N);
    }

    @PostMapping("/getHistory")
    public ApiResponse<List<ContentResonse>> getHistory(HttpServletRequest request, @RequestBody long pushTime, long lastContentId, int N) {
        long userId = (long) request.getAttribute("userId");
        // 获取历史推送消息
        return timelineMessageGetServer.getHistoryResponse(userId, pushTime, lastContentId, N);
    }

}
