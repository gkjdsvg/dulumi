package com.example.dulumi.sse;

import com.example.dulumi.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@ToString(exclude = "receiver")
public class TestNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String content;

    private String url;

    private String toName; //대상

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User receiver;

    @Builder
    public TestNotification(User receiver, NotificationType notificationType, String content, String url, String toName) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;
        this.toName = toName;
        this.url = url;
    }
}
