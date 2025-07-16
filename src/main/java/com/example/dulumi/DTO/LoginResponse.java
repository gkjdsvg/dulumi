package com.example.dulumi.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String email;

    public LoginResponse(String email, String username, String token) {
        this.email = email;
        this.username = username;
        this.token = token;
    }
}
