package com.example.dulumi.config.JWT;


import com.auth0.jwt.exceptions.TokenExpiredException;

import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.domain.PrincipalDetails;
import com.example.dulumi.domain.User;
import com.example.dulumi.service.PrincipalService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.rmi.RemoteException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PrincipalService principalService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = JwtProvider.resolveToken(request);
            System.out.println("💡 추출된 토큰: " + token);

            try {
                if (token != null && jwtProvider.validateToken(token)) {
                    Authentication auth = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("✅ SecurityContext 등록 완료 : " + auth);
                }
            } catch (TokenExpiredException e) {
                // 여기서 refresh_token으로 복구 시도 (옵션)
                String refreshToken = request.getHeader("REFRESH_TOKEN");
                if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
                    String username = jwtProvider.getUsername(refreshToken);
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//                    String newAccessToken = jwtProvider.createAccessToken(user);
//                    String newRefreshToken = JwtProvider.createRefreshToken(user, newAccessToken);
//                    PrincipalDetails principalDetails = new PrincipalDetails(user);

                    UserDetails userDetails = principalService.loadUserByUsername(username);
//                    response.setHeader("ACCESS_TOKEN", newAccessToken);
//                    response.setHeader("REFRESH_TOKEN", newRefreshToken);

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }else {
                    System.out.println("리프레시 토큰으로 재발급 완료");
                }
            } catch (Exception e) {
                System.out.println("인증 실패 : " + e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }
}
