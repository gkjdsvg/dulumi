package com.example.dulumi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class AnnouncementDto {
    private String entity;
    private String title;
}
