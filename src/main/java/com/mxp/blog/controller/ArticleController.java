package com.mxp.blog.controller;

import com.github.pagehelper.PageInfo;
import com.mxp.blog.model.Article;
import com.mxp.blog.model.Comment;
import com.mxp.blog.service.ArticleService;
import com.mxp.blog.service.CommentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * EMAIL menxipeng@gmail.com
 * AUTHOR:menxipeng
 * DATE: 2019/5/21
 * TIME: 9:56
 */
@Log4j2
@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        System.out.println("进来了");
        return "index";
    }

    @GetMapping("/details/{id}")
    public String details(HttpServletRequest request, @PathVariable("id") Integer id) {
        var result = new DeferredResult<PageInfo>();
        Article article = this.articleService.selectById(id).get();
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        request.setAttribute("article", article);
        return "details";
    }

    @ResponseBody
    @PostMapping("/comment")
    //查询文章所有评论
    public DeferredResult<PageInfo> comment(@RequestBody Comment comment) {
        var result = new DeferredResult<PageInfo>();
        CompletableFuture.supplyAsync(() -> this.commentService.findCommentByArticleId(comment.getId(), comment.getCurr()).
                        map(comments -> {
                            //将数据加入到pageInfo中,连续显示的页数
                            PageInfo pageInfo = new PageInfo(comments, 5);
                            result.setResult(pageInfo);
                            return "Success";
                        }).orElseGet(() -> {
                    log.warn("评论查询数据为空");
                    return "Error";
                })
        ).orTimeout(20000, TimeUnit.MILLISECONDS).exceptionally(e -> {
            log.error("评论标签页异常", e);
            return null;
        });
        return result;
    }

    @ResponseBody
    @PostMapping("/incComment")
    public DeferredResult<Map> incComment(@RequestBody Comment comment) {
        Map map = new HashMap<String, Object>();
        var result = new DeferredResult<Map>();
        CompletableFuture.supplyAsync(() -> {
            int status = this.commentService.addComment(comment);
            if (status > 0) {
                map.put("status", 0);
            } else {
                map.put("status", 1);
            }
            result.setResult(map);
            return "Success";
        }).orTimeout(20000, TimeUnit.MILLISECONDS).exceptionally(e -> {
            log.error("评论异常", e);
            return null;
        });
        return result;
    }


    @ResponseBody
    @PostMapping("/breadcrumbData")
    public DeferredResult<PageInfo> breadcrumbData(@RequestBody Article article) {
        var result = new DeferredResult<PageInfo>();
        //使用分页先调用PageHelper方法
        CompletableFuture.supplyAsync(() -> this.articleService.selectAllByType(article)
                .map(articles -> {
                    //将数据加入到pageInfo中,连续显示的页数
                    PageInfo pageInfo = new PageInfo(articles, 5);
                    result.setResult(pageInfo);
                    return "Success";
                }).orElseGet(() -> {
                    log.warn("查询数据为空");
                    return "Error";
                })
        ).orTimeout(20000, TimeUnit.MILLISECONDS).exceptionally(e -> {
            log.error("查询标签页异常", e);
            return null;
        });
        return result;
    }

}
