package com.example.dulumi.config.JWT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JwtDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static JwtDto res(String grantType, String accessToken, String refreshToken) {
        return new JwtDto(grantType, accessToken, refreshToken);
    }
}
