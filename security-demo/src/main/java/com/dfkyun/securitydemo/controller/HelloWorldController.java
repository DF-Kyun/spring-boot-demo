package com.dfkyun.securitydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        return "/index.html";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

}
