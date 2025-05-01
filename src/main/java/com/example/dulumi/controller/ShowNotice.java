package com.example.dulumi.controller;

import com.example.dulumi.Repository.NoticeRepository;
import com.example.dulumi.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class ShowNotice {
    private final NoticeRepository noticeRepository;

    @PostMapping("/api/notice")
    public ResponseEntity<List<Notice>> showNotice() {
        List<Notice> noticeList = noticeRepository.findAll();
        return ResponseEntity.ok(noticeList);
    }
}
