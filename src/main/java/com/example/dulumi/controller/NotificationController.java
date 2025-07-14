package com.example.dulumi.controller;

import com.example.dulumi.DTO.MsgResponseDto;
import com.example.dulumi.config.auth.UserDetailsImpl;
import com.example.dulumi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping("/api/notification/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();

        return notificationService.subscribe(userId);
    }

    @DeleteMapping("/api/notification/delete/{id}")
    public MsgResponseDto deleteNotification(@PathVariable Long id) throws IOException {
        return notificationService.deleteNotification(id);
    }
}
