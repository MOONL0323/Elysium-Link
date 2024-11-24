package com.example.demo.articles.service.impl;

import com.example.demo.articles.entity.Article;
import com.example.demo.articles.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Override
    public List<Article> listArticles(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Article getArticle(Long id) {
        return null;
    }

    @Override
    public Article addArticle(Article article) {
        return null;
    }

    @Override
    public Article updateArticle(Article article) {
        return null;
    }

    @Override
    public void deleteArticle(Long id) {

    }
}
