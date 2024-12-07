package com.example.demo.articles.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.articles.dao.ArticleMapper;
import com.example.demo.articles.entity.Article;
import com.example.demo.articles.service.ArticleService;
import com.example.demo.userRelationships.service.FollowerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleService {

    FollowerService followerService;
    @Override
    public List<Article> listArticles(int pageNum, int pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        return this.page(page).getRecords();
    }

    @Override
    public Article getArticle(Long id) {
        return this.getById(id);
    }

    @Override
    public Article addArticle(Article article) {
        this.save(article);

        /**
         * 推送给粉丝列表的用户的消息队列中
         */
        List<Long> fans=followerService.getFollower(article.getAuthorId());

        for(Long fan:fans){
            //TODO:推送消息
        }

        return article;
    }

    @Override
    public Article updateArticle(Article article) {
        this.updateById(article);
        return article;
    }

    @Override
    public void deleteArticle(Long id) {
        this.removeById(id);
    }

}
