package com.example.demo.articles.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.articles.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 86188
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
