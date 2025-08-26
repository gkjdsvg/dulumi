package com.example.dulumi.Kakao;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoController {
    @GetMapping("/kakao/login")
    public String kakaoLogin(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return "로그인 성공! 사용자 정보: " + oAuth2User.getAttributes();
    }
}
