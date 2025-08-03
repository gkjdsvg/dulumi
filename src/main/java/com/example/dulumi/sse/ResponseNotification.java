package com.example.dulumi.sse;

import lombok.Getter;

@Getter
public class ResponseNotification {
    private final String content;
    private final String url;
    private final String type;
    private final String toName;

    public ResponseNotification(String content, String url, String type, String toName) {
        this.content = content;
        this.url = url;
        this.type = type;
        this.toName = toName;
    }

    public static ResponseNotification from(TestNotification notification) {
        return new ResponseNotification(
                notification.getContent(),
                notification.getUrl(),
                notification.getNotificationType().name(),
                notification.getToName()
        );
    }
}
