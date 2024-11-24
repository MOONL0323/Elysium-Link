package com.example.demo.articles.service;

import com.example.demo.articles.entity.Article;

import java.util.List;


public interface ArticleService {
    /**
     * 获取文章列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 文章列表
     */
    List<Article> listArticles(int pageNum, int pageSize);

    /**
     * 获取文章详情
     * @param id 文章id
     * @return 文章详情
     */
    Article getArticle(Long id);

    /**
     * 新增文章
     * @param article 文章
     * @return 新增的文章
     */
    Article addArticle(Article article);

    /**
     * 更新文章
     * @param article 文章
     * @return 更新后的文章
     */
    Article updateArticle(Article article);

    /**
     * 删除文章
     * @param id 文章id
     */
    void deleteArticle(Long id);
}
