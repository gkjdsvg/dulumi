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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

//    public JwtAuthenticationFilter(UserRepository userRepository, JwtProvider jwtProvider) {
//        this.userRepository = userRepository;
//        this.jwtProvider = jwtProvider;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse  response, FilterChain chain) throws IOException, ServletException {
        String access_token = request.getHeader("ACCESS_TOKEN");
        String refresh_token = request.getHeader("REFRESH_TOKEN");
        String username = null;
        String restoreAccessToken = null;
        String token = jwtProvider.resolveToken(request);
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")) {
            doFilter(request, response,  chain);
            return;
        }

        if (token != null && jwtProvider.validateToken(token)) {
            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

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
                String accessToken = JwtProvider.createAccessToken(user);
                String refreshToken = JwtProvider.createRefreshToken(user, accessToken);

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
