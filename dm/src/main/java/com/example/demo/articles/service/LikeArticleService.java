package com.example.demo.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.articles.entity.LikeArticle;

public interface LikeArticleService extends IService<LikeArticle> {
    /**
     * 点赞文章
     * @param id 文章id
     */
    void likeArticle(Long id);
    /**
     * 取消点赞文章
     * @param id 文章id
     */
    void unlikeArticle(Long id);
}
