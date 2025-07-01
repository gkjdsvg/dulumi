package com.example.dulumi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class User {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; //ADMIN, MANAGER, USER
    //private String role;

    private String provider; //어떤 OAuth인지(google, naver 등)

    private String provider_id; // 해당 OAuth 의 key(id)

    private LocalDateTime create_date;

    @Builder
    public User(String username, String password, String email, Role role, String provider, String provider_id, LocalDateTime create_date) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.provider_id = provider_id;
        this.create_date = create_date;
    }
}
