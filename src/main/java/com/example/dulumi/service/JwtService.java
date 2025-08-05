package com.example.dulumi.service;

import com.example.dulumi.Repository.JwtRepository;
import com.example.dulumi.domain.Jwt;
import com.example.dulumi.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtRepository jwtRepository;

    @Transactional
    public void saveOrUpdateToken(User user, String accessToken, String refreshToken) {
        jwtRepository.findByUser(user)
                .ifPresentOrElse(
                        existing -> {
                            existing.updateToken(accessToken, refreshToken);
                        },
                        () -> {
                            Jwt jwt = Jwt.builder()
                                    .user(user)
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .grantType("Bearer")
                                    .build();
                            jwtRepository.save(jwt);
                            System.out.println("✅ 저장 직전 JWT 객체 : " + jwt.getGrantType());
                        }
                );
        }
}
