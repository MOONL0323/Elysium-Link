package com.example.demo.content.controller;

import com.example.demo.content.entity.ContentRequest;

import com.example.demo.content.entity.ContentResonse;
import com.example.demo.content.service.ContentServer;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.MinIoUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContentController {

    @Autowired
    private MinIoUtil minIoUtil;

    @Autowired
    private ContentServer contentServer;

    /**
     * 上传内容
     * @param content
     * @param files
     * @return
     */
    @PostMapping("/uploadContent")
    public Map<String, Map<String, String>> uploadContent(@RequestParam("content") String content,
                                             @RequestParam(value = "files", required = false) MultipartFile[] files) {
        Map<String, String> imageUrls = new HashMap<>();
        Map<String, String> textUrls = new HashMap<>();

        try {
            if (files != null) {
                for (MultipartFile file : files) {
                    String fileUrl = minIoUtil.upload("go-backend", file);
                    if (file.getContentType().startsWith("image/")) {
                        imageUrls.put(file.getOriginalFilename(), fileUrl);
                    } else if (file.getContentType().equals("text/plain")) {
                        textUrls.put(file.getOriginalFilename(), fileUrl);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Map<String, String>> result = new HashMap<>();
        result.put("images", imageUrls);
        result.put("texts", textUrls);

        return result;
    }

    /**
     * 新增内容
     * @param contentRequest
     * @return
     */
    @PostMapping("/post")
    public void addContent(@RequestBody ContentRequest contentRequest) {
        // 处理请求参数
        contentServer.addContent(contentRequest);
        return;
    }

    /**
     * 更新内容
     * @param contentRequest
     * @return
     */
    @PostMapping("/update")
    public void updateContent(@RequestBody ContentRequest contentRequest,@RequestBody long itemId) {
        // 处理请求参数
        contentServer.updateContent(contentRequest,itemId);
        return;
    }

    /**
     * 删除内容，用户只能删除自己的内容
     * @param itemId
     * @return
     */
    @PostMapping ("/delete/{itemId}")
    public void deleteContent(@RequestBody long itemId) {
        // 处理请求参数
        contentServer.userDeleteContent(itemId);
        return;
    }

    /**
     * 后台管理人员下架内容
     * @param itemId
     * @return
     */
    @PostMapping ("/offline/{itemId}")
    public void offlineContent(@RequestBody long itemId) {
        // 处理请求参数
        contentServer.adminOfflineContent(itemId);
        return;
    }

    /**
     * 获取文章列表
     */
    @GetMapping("/reader")
    public ApiResponse<List<Long>> getReader(@RequestBody long creatorId, @RequestBody int page, @RequestBody int size) {
        return contentServer.getReader(creatorId,page,size);

    }

    /**
     * 文章内容详细信息
     * @param itemId
     * @return ApiResponse
     */
    @GetMapping("")
    public ApiResponse<ContentResonse> getContent(@RequestBody long itemId) {
        return contentServer.getContent(itemId);
    }

    /**
     * 获取我的文章列表
     * @return ApiResponse
     */
    @GetMapping("/writer")
    public ApiResponse<List<Long>> getWriter(HttpServletRequest request,  @RequestBody int page, @RequestBody int size) {
        long creatorId = Long.parseLong(request.getHeader("userId"));
        return contentServer.getReader(creatorId,page,size);
    }

}
