package com.example.demo.articles.dao;

import com.example.demo.articles.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleMapper {
    @Select("select * from article where aid = #{aid}")
    Article getArticleById(Long aid);

}
