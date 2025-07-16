package com.example.dulumi.config.JWT;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.domain.PrincipalDetails;
import com.example.dulumi.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
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
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = JwtProvider.resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        try {
            if (jwtProvider.validateToken(token)) {
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (TokenExpiredException e) {
            // 여기서 refresh_token으로 복구 시도 (옵션)
            String refreshToken = request.getHeader("REFRESH_TOKEN");
            if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
                String username = jwtProvider.getUsername(refreshToken);
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                String newAccessToken = JwtProvider.createAccessToken(user);
                String newRefreshToken = JwtProvider.createRefreshToken(user, newAccessToken);
                PrincipalDetails principalDetails = new PrincipalDetails(user);

                response.setHeader("ACCESS_TOKEN", newAccessToken);
                response.setHeader("REFRESH_TOKEN", newRefreshToken);

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        new PrincipalDetails(user), null, principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
