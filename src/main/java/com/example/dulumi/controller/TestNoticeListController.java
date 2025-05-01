package com.example.dulumi.controller;

import com.example.dulumi.domain.Notice;
import com.example.dulumi.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
@AllArgsConstructor
public class TestNoticeListController {
    private final NoticeService noticeService;

    @PostMapping
    public List<Notice> getNoticeList() {
        return noticeService.getAllNotices();
    }

}
