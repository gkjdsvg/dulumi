package com.example.dulumi.service;


import com.example.dulumi.DTO.MsgResponseDto;
import com.example.dulumi.Repository.NotificationRepository;
import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.controller.NotificationController;
import com.example.dulumi.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.dulumi.controller.NotificationController.sseEmitters;

@ComponentScan(basePackages = "com.example.dulumi.DTO")
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private static Map<Long, Integer> notificationCounts = new HashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        try {
            emitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sseEmitters.put(userId, emitter);

        emitter.onCompletion(() -> sseEmitters.remove(userId));
        emitter.onTimeout(() -> sseEmitters.remove(userId));
        emitter.onError((e) -> sseEmitters.remove(userId));

        return emitter;
    }

    public void notifyNewNotice() {
        for (Map.Entry<Long, SseEmitter> entry : sseEmitters.entrySet()) {
            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("message", "새 공지가 떴어요");

                entry.getValue().send(SseEmitter.event()
                        .name("newNotice")
                        .data(eventData));
            } catch (Exception e) {
                sseEmitters.remove(entry.getKey());
            }
        }
    }

    public MsgResponseDto deleteNotification(Long id) throws IOException {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("알림을 찾을 수 없습니다.")
        );

        Long userId = notification.getPost().getUser().getId();

        notificationRepository.delete(notification);

        // 알림 개수 감소
        if (notificationCounts.containsKey(userId)) {
            int currentCount = notificationCounts.get(userId);
            if (currentCount > 0) {
                notificationCounts.put(userId, currentCount - 1);
            }
        }

        // 현재 알림 개수 전송
        SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
        sseEmitter.send(SseEmitter.event().name("notificationCount").data(notificationCounts.get(userId)));

        return MsgResponseDto.builder()
                .message("알림이 삭제 되었습니다")
                .type("Success")
                .sendAt(LocalDateTime.now())
                .build();
    }
}
