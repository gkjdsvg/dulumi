package com.example.dulumi.DTO;

import com.example.dulumi.domain.Announcement;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AnnouncementDto {
    private String title;
    private String content;

    public Announcement toEntity() {
        return Announcement.builder()
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
