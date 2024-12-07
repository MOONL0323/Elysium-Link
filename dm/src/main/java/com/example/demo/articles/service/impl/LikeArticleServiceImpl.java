package com.example.demo.articles.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.articles.dao.LikeArticleMapper;
import com.example.demo.articles.entity.Article;
import com.example.demo.articles.entity.LikeArticle;
import com.example.demo.articles.service.ArticleService;
import com.example.demo.articles.service.LikeArticleService;
import org.springframework.stereotype.Service;

@Service
public class LikeArticleServiceImpl extends ServiceImpl<LikeArticleMapper, LikeArticle> implements LikeArticleService {

    ArticleService articleService;
    @Override
    public void likeArticle(Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return;
        }
        article.setLikeCount(article.getLikeCount() + 1);
        articleService.updateById(article);

        LikeArticle likeArticle = new LikeArticle();
        likeArticle.setArticleId(id);
        this.save(likeArticle);

        //TODO:推送消息

    }

    @Override
    public void unlikeArticle(Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return;
        }
        article.setLikeCount(article.getLikeCount() - 1);
        articleService.updateById(article);

        this.removeById(id);

    }
}
