package com.example.dulumi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.intellij.lang.annotations.Identifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;


import javax.annotation.processing.Generated;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private LocalDateTime date;
}
