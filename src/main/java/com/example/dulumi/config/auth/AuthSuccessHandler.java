package com.example.dulumi.config.auth;

import com.example.dulumi.DTO.JwtDto;
import com.example.dulumi.DTO.LoginResponse;
import com.example.dulumi.DTO.ResponseDTO;
import com.example.dulumi.config.JWT.JwtProvider;
import com.example.dulumi.domain.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //oauth2 로그인이 성공 했을 때의 추가 작업 수행
        //여기서는 JWT 발급하고 형식에 맞게 리턴
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String token = jwtProvider.createAccessToken(principal.getUser());

        LoginResponse loginResponse = new LoginResponse(
                principal.getUser().getEmail(),
                principal.getUser().getUsername(),
                token
        );

        ResponseDTO<LoginResponse> responseDto = ResponseDTO.res(
                HttpServletResponse.SC_OK,
                "Login successful",
                loginResponse
        );

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(responseDto);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
