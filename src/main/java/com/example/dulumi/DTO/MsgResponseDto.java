package com.example.dulumi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MsgResponseDto {
    private Long userId;
    private String message;
    private String type;
    private LocalDateTime sendAt;
}
