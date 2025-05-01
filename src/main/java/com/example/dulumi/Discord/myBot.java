package com.example.dulumi.Discord;

import com.example.dulumi.Repository.NoticeRepository;
import com.example.dulumi.domain.Notice;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.EnumSet;

@Component
@RequiredArgsConstructor
public class myBot extends ListenerAdapter {
    private final NoticeRepository noticeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private String lastMessageId = "";

    @PostConstruct
    public void startBot() throws Exception {
        JDABuilder.createDefault("디스코드 토큰",
                        EnumSet.of(
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT
                        ))
                .addEventListeners(this)
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

        if (channel.getName().equals("공지")) {
            String currentMessageId = event.getMessage().getId();
            if (currentMessageId.equals(lastMessageId)) return;

            lastMessageId = currentMessageId;

            String content = event.getMessage().getContentDisplay();
            System.out.println("새 공지 : " + content);

            // ✅ DB 저장
            Notice notice = new Notice();
            notice.setContent(content);
            notice.setDate(LocalDateTime.now());
            noticeRepository.save(notice);

            // ✅ 실시간 전송
            messagingTemplate.convertAndSend("/notices", content);
        }
    }
}
