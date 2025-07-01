package com.example.dulumi.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Configuration
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            System.out.println("✅ 폼 로그인 성공: " + userDetails.getUsername());

        } else if (principal instanceof OAuth2User oauthUser) {
            System.out.println("✅ 구글 로그인 성공: " + oauthUser.getAttributes());
        } else {
            System.out.println("⚠️ 알 수 없는 로그인 방식");
        }

        // 로그인 후 리다이렉트
        response.sendRedirect("/main");
    }
}
