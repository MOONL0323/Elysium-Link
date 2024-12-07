package com.example.demo.articles.controller;


import com.example.demo.articles.entity.Article;
import com.example.demo.articles.entity.CollectArticle;
import com.example.demo.articles.service.ArticleService;
import com.example.demo.articles.service.CollectArticleService;
import com.example.demo.articles.service.LikeArticleService;
import com.example.demo.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private LikeArticleService likeArticleService;
    @Autowired
    private CollectArticleService collectArticleService;

    @GetMapping
    public ApiResponse<?> listArticles(int pageNum, int pageSize) {
        articleService.listArticles(pageNum, pageSize);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getArticle(@PathVariable Long id) {
        articleService.getArticle(id);
        return ApiResponse.ok(null);
    }

    @PostMapping
    public ApiResponse<?> addArticle(@RequestBody Article article) {
        articleService.addArticle(article);
        return ApiResponse.ok(article);
    }

    @PostMapping
    public ApiResponse<?> updateArticle(@RequestBody Article article) {
        articleService.updateArticle(article);
        return ApiResponse.ok(article);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/like/{id}")
    public ApiResponse<?> likeArticle(@PathVariable Long id) {
        likeArticleService.likeArticle(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/unlike/{id}")
    public ApiResponse<?> unlikeArticle(@PathVariable Long id) {
        likeArticleService.unlikeArticle(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/collect/{id}")
    public ApiResponse<?> collectArticle(@PathVariable Long id) {
        collectArticleService.collectArticle(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/uncollect/{id}")
    public ApiResponse<?> uncollectArticle(@PathVariable Long id) {
        collectArticleService.uncollectArticle(id);
        return ApiResponse.ok(null);
    }

}
