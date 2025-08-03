package com.example.dulumi.sse;

import com.example.dulumi.domain.PrincipalDetails;
import com.example.dulumi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class NotificationController {
    private final TestNotificationService notificationService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestHeader(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId) {

        Long userId = principalDetails.getUser().getId();
        SseEmitter emitter = notificationService.subscribe(userId, lastEventId);
        return ResponseEntity.ok(emitter);
    }
}
