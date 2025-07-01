package com.example.dulumi.controller;

import com.example.dulumi.DTO.AnnouncementDto;
import com.example.dulumi.domain.User;
import com.example.dulumi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Controller
@RestController
@Slf4j

public class AlramController {
    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    //메세지 알림
    @GetMapping("api/notification/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        SseEmitter emitter = notificationService.subscribe(userId);

        return emitter;
    }

    @PostMapping("/announcements")
    public ResponseEntity<Void> createAnnouncement(@RequestBody AnnouncementDto dto) {
        notificationService.sendToAll();
        return ResponseEntity.ok().build();
    }
}
