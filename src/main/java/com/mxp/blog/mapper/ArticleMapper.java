package com.mxp.blog.mapper;

import com.mxp.blog.model.Article;
import com.mxp.blog.model.Photo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * EMAIL menxipeng@gmail.com
 * AUTHOR:menxipeng
 * DATE: 2019/5/23
 * TIME: 14:23
 */
@Mapper
public interface ArticleMapper {
    List<Article> selectAllByType(Article article);
    int insertArticle(Article article);
    Article selectById(Integer id);
    int inserPhoto(Article article);
}
