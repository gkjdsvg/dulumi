package com.example.dulumi.config.JWT;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    static String SECRET = ""; // 우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 60000*10; // 10분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    public void setSecret(String secret) {
        JwtProperties.SECRET = secret;
    }
}
