package com.example.demo.articles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.articles.dao.CollectArticleMapper;
import com.example.demo.articles.entity.Article;
import com.example.demo.articles.entity.CollectArticle;
import com.example.demo.articles.service.ArticleService;
import com.example.demo.articles.service.CollectArticleService;
import org.springframework.stereotype.Service;

@Service
public class CollectArticleServiceImpl extends ServiceImpl<CollectArticleMapper, CollectArticle> implements CollectArticleService {

    ArticleService articleService;
    @Override
    public void collectArticle(Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return;
        }
        article.setCollectCount(article.getCollectCount() + 1);
        articleService.updateById(article);

        CollectArticle collectArticle = new CollectArticle();
        collectArticle.setArticleId(id);
        this.save(collectArticle);
        //TODO:推送消息
    }

    @Override
    public void uncollectArticle(Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return;
        }
        article.setCollectCount(article.getCollectCount() - 1);
        articleService.updateById(article);

        this.removeById(new QueryWrapper<CollectArticle>().eq("article_id", id));
    }
}
