package com.mxp.blog.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * EMAIL menxipeng@gmail.com
 * AUTHOR:menxipeng
 * DATE: 2019/5/24
 * TIME: 17:34
 */
@Log4j2
@Controller
public class AboutController {
    @GetMapping("/about")
    public String about(HttpServletRequest request) {
        System.out.println("进来了");
        return "about";
    }
}
