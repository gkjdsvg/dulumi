package com.example.dulumi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

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
