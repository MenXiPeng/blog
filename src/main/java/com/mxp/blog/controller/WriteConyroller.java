package com.mxp.blog.controller;

import com.google.gson.Gson;
import com.mxp.blog.model.Article;
import com.mxp.blog.service.ArticleService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * EMAIL menxipeng@gmail.com
 * AUTHOR:menxipeng
 * DATE: 2019/5/28
 * TIME: 13:29
 */
@Log4j2
@Controller
public class WriteConyroller {

    @Autowired
    private ArticleService articleService;

    @ResponseBody
    @RequestMapping("/write")
    public String write(HttpServletRequest request, HttpSession session) {
        Map map = new HashMap<String,Object>();
        map.put("status", 10000);
        Gson gson = new Gson();
        String result = gson.toJson(map);
        System.out.println("进来了");
        return result;
    }

    @RequestMapping("/write/toWrite")
    public String toWrite(HttpServletRequest request, HttpSession session) {
        System.out.println("进来了");
        return "write";
    }



    @ResponseBody
    @PostMapping("/releaseContext")
    public DeferredResult<Map> releaseContext(@RequestBody Article article) {
        var map = new HashMap<String, Integer>();
        var result = new DeferredResult<Map>();
        CompletableFuture.supplyAsync(() -> {
            article.setCreateTime(LocalDateTime.now());
            int status = this.articleService.addArticle(article);
            if (status > 0) {
                map.put("status", 0);
            } else {
                map.put("status", 1);
            }
            result.setResult(map);
            return "Success";
        }).orTimeout(20000, TimeUnit.MILLISECONDS)
                .exceptionally(e -> {
                    log.error("发布文章错误", e);
                    return null;
                });
        return result;
    }
}
