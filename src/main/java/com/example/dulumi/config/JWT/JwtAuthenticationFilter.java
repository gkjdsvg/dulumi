package com.example.dulumi.config.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.domain.PrincipalDetails;
import com.example.dulumi.domain.User;
import com.example.dulumi.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

import com.auth0.jwt.JWT;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse  response, FilterChain chain) throws IOException, ServletException {
        String access_token = request.getHeader("ACCESS_TOKEN");
        String refresh_token = request.getHeader("REFRESH_TOKEN");
        String username = null;
        String restoreAccessToken = null;

        try {
            username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                    .build()
                    .verify(access_token)
                    .getClaim("username")
                    .asString();

            restoreAccessToken = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                    .build()
                    .verify(refresh_token)
                    .getClaim("AccessToken")
                    .asString();
        } catch (TokenExpiredException e) {
            String restoreUsername = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                    .build()
                    .verify(refresh_token)
                    .getClaim("username")
                    .asString();
            if (restoreUsername != null && Objects.equals(restoreAccessToken, access_token)) {
                User user = userRepository.findByUsername(restoreUsername)
                        .orElseThrow(() -> new UsernameNotFoundException("username not found"));
                String accessToken = CreateJwt.createAccessToken(user);
                String refreshToken = CreateJwt.createRefreshToken(user, accessToken);

                response.setHeader("ACCESS_TOKEN", accessToken);
                response.setHeader("REFRESH_TOKEN", refreshToken);
            }
        }
        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("username not found"));
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
