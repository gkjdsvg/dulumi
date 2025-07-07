//package com.example.dulumi.service;
//
//import com.example.dulumi.DTO.AnnouncementDto;
//import com.example.dulumi.Repository.AnnouncementRepository;
//import com.example.dulumi.Repository.UserRepository;
//import com.example.dulumi.controller.AlramController;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//@ComponentScan(basePackages = "com.example.dulumi.DTO")
//@Service
//@RequiredArgsConstructor
//public class NotificationService {
//    private final AnnouncementRepository announcementRepository;
//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//
//    //메세지 알림
//    public SseEmitter subscribe(Long userId) {
//        //1. 현재 클라이언트를 위한 SseEmitter 객체 생성
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//
//        //2. 연결
//        try {
//            emitter.send(SseEmitter.event().name("connect"));
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        //3. 저장
//        AlramController.sseEmitters.put(userId, emitter);
//
//        //4. 연결 종료 처리
//        emitter.onCompletion(() -> AlramController.sseEmitters.remove(userId));
//        emitter.onTimeout(() -> AlramController.sseEmitters.remove(userId));
//        emitter.onError((e) -> AlramController.sseEmitters.remove(userId));
//
//        return emitter;
//    }
//
//    //알림 전송
//    public void sendToAll() {
//        AnnouncementDto dto = new AnnouncementDto();
//        //공지 저장
//        announcementRepository.save(dto.toEntity());
//
//        //전체 사용자에게 알림 전송
//        emitters.forEach((userId, emitter) -> {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("announcement")
//                        .data("📢 새 공지 : " + dto.getTitle()));
//            }catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        });
//    }
//}
