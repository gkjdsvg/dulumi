package com.example.dulumi.sse;

import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import static java.util.stream.StreamSupport.stream;

@Service
@Transactional
@RequiredArgsConstructor
public class TestNotificationService {
    private final EmitterRepository emitterRepository;
    private final TestNotificationRepository testNotificationRepository;
    private final UserRepository userRepository;

    //연결 지속 시간 ＝ 한 시간
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(Long userId, String lastEventId) {
        //고유한 아이디 생성
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        //시간 초과나 비동기 요펑이 안되면 자동으로 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        //최초 연결시 더미 데이터가 없으면 ５０３ 오류가 발생하기 때문에 해당 더미 데이터 생성
        sendToClient(emitter, emitterId, "EventStream Created. [userId = " + userId + "]");

        //ｌａｓｔＥｖｅｎｔＩｄ 있다는 것은 연결이 졸료 됐다 그래서 해당 데이터가 남아있는지 살펴보고 있다면 남은 데이터 전송
        if(!lastEventId.isEmpty()) {
            Map<String, SseEmitter> events = emitterRepository.findAllEmitterStartWithByMemberId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey())<0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

    public void send(User receiver, NotificationType notificationType, String content, String url, String toName) {
        TestNotification notification = testNotificationRepository.save(createNotification(receiver, notificationType, content, url, toName));
        String UserId = String.valueOf(receiver.getId());

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(UserId);

        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, new ControllerApiResponse<>(true, "새로운 알림" + ResponseNotification.from(notification)));
                }
        );
    }

    private TestNotification createNotification(User receiver, NotificationType notificationType, String content, String url, String toName) {
        return TestNotification.builder().receiver(receiver).notificationType(notificationType).content(content).url(url).toName(toName).build();
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            throw new RuntimeException("전송 실패");
        }
    }
}
