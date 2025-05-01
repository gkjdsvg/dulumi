package com.example.dulumi.service;

import com.example.dulumi.Repository.NoticeRepository;
import com.example.dulumi.domain.Notice;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }
}
