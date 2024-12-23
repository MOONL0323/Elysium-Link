package com.example.demo.messagepush.service.impl;

import com.example.demo.content.entity.ContentResonse;
import com.example.demo.content.service.impl.ContentServerImpl;
import com.example.demo.messagepush.entity.InboxInfoPo;
import com.example.demo.messagepush.dao.InboxMapper;
import com.example.demo.messagepush.service.TimelineMessageGetServer;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimelineMessageGetServerImpl implements TimelineMessageGetServer {

    @Autowired
    private InboxMapper inboxMapper;

    @Autowired
    private ContentServerImpl contentServer;

    public ApiResponse<List<ContentResonse>> getNewResponse(long userId,int N) {
        List<ContentResonse> contentResonseList = getNew(userId,N);
        return ApiResponse.ok(contentResonseList);
    }

    private List<ContentResonse> getNew(long userId,int N) {
        List<InboxInfoPo> inboxInfoPoList = inboxMapper.getInboxNew(userId,N);
        //循环获取content并封装成List
        List<ContentResonse> contentResonseList = new ArrayList<>();
        for (InboxInfoPo inboxInfoPo : inboxInfoPoList) {
            ContentResonse contentResonse = contentServer.getAContent(inboxInfoPo.getContentId());
            contentResonseList.add(contentResonse);
        }
        return contentResonseList;
    }

    @Override
    public ApiResponse<List<ContentResonse>> getHistoryResponse(long userId,long pushTime,long lastContentId,int N) {
        List<ContentResonse> contentResonseList = getHistory(userId,pushTime,lastContentId,N);
        return ApiResponse.ok(contentResonseList);
    }

    private List<ContentResonse> getHistory(long userId,long pushTime,long lastContentId,int N) {
        List<InboxInfoPo> inboxInfoPoList = inboxMapper.getInboxHistory(userId,pushTime,lastContentId,N);
        //循环获取content并封装成List
        List<ContentResonse> contentResonseList = new ArrayList<>();
        for (InboxInfoPo inboxInfoPo : inboxInfoPoList) {
            ContentResonse contentResonse = contentServer.getAContent(inboxInfoPo.getContentId());
            contentResonseList.add(contentResonse);
        }
        return contentResonseList;
    }
}
