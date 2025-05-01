package com.example.dulumi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class viewController {
    @GetMapping("/main")
    public String home() {
        return "main";
    }

    @GetMapping("/notice")
    public String test() {
        return "noticeList";
    }

    @GetMapping("/test/notice")
    public String test2() {
        return "noticeListTest";
    }
}
