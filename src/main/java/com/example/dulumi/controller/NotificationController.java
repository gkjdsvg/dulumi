package com.example.dulumi.controller;

import com.example.dulumi.DTO.MsgResponseDto;
import com.example.dulumi.Repository.JwtRepository;
import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.config.JWT.JwtProvider;
import com.example.dulumi.domain.Jwt;
import com.example.dulumi.domain.User;
import com.example.dulumi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtProvider jwtProvider;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;

//    @GetMapping("/api/notification/subscribe")
//    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Long userId = userDetails.getUser().getId();
//
//        return notificationService.subscribe(userId);
//    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/notification/subscribe")
    public SseEmitter subscribe(@RequestParam String token) {
        System.out.println("🔥 subscribe 진입");
        System.out.println("🔥 token = " + token);

        Long userId = jwtProvider.getUserIdFromToken(token); //이 줄에서 예외 나면 401
        return notificationService.subscribe(userId);
    }

    @GetMapping("api/dev-token")
    public ResponseEntity<String> getDevToken(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Jwt jwt = jwtRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("토큰 없음"));

        return ResponseEntity.ok(jwt.getAccessToken());
    }

    @DeleteMapping("/api/notification/delete/{id}")
    public MsgResponseDto deleteNotification(@PathVariable Long id) throws IOException {
        return notificationService.deleteNotification(id);
    }

    @GetMapping("/api/notification/test")
    public ResponseEntity<String> sendTestNotification() {
        notificationService.notifyNewNotice();
        return ResponseEntity.ok("알림 보냄!");
    }
}
