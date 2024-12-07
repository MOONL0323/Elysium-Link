package com.example.demo.articles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.articles.entity.CollectArticle;

public interface CollectArticleService extends IService<CollectArticle> {
    /**
     * 收藏文章
     * @param id 文章id
     */
    void collectArticle(Long id);
    /**
     * 取消收藏文章
     * @param id 文章id
     */
    void uncollectArticle(Long id);
}
