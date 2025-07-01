package com.example.dulumi.config.JWT;

import com.example.dulumi.DTO.AddUserRequest;
import com.example.dulumi.DTO.jwtDto;
import com.example.dulumi.Repository.UserRepository;
import com.nimbusds.jwt.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.nimbusds.oauth2.sdk.RefreshTokenGrant.GRANT_TYPE;

@Component
public class JWTUtil {

    private final Key key;



    // application.yml에서 secret 값 가져와서 key에 저장
    public JWTUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public jwtDto generateToken(Long userId) {
        // 권한 가져오기
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 1800000);

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
//                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 1209600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwtDto.builder()
                .grantType(GRANT_TYPE.getValue())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
