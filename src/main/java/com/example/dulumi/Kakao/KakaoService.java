package com.example.dulumi.Kakao;

import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.config.JWT.JWTUtil;
import com.example.dulumi.config.JWT.JwtProperties;
import com.example.dulumi.config.JWT.JwtProvider;
import com.example.dulumi.domain.User;
import com.example.dulumi.info.KakaoUserInfo;
import com.example.dulumi.info.OAuth2UserInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final JwtProvider jwtProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String id = attributes.get("id").toString();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(kakaoAccount, id);

        // 1. DB 조회 or 저장
        User user = userRepository.findByUsername(kakaoUserInfo.getProviderId())
                .orElseGet(() -> {
                    User newUser = new User(
                            kakaoUserInfo.getEmail(),
                            kakaoUserInfo.getProvider(),
                            kakaoUserInfo.getProviderId()
                    );
                    return userRepository.save(newUser);
                });

        // 2. JWT 발급
        String jwt = jwtProvider.createAccessToken(user);

        // 3. Security 세션 저장용 OAuth2User 리턴
        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                "id"
        );
    }
}
