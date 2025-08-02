package com.example.dulumi.config.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.dulumi.Repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
//    private final Key key;
    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);
    private static final long ACCESS_TOKEN_VALID_TIME = 1000 * 60 * 60L;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        System.out.println("secret key initialized : " + key);
    }

    // application.yml에서 secret 값 가져와서 key에 저장
//    public JwtProvider(@Value("${jwt.secret}") String secretKey, UserRepository userRepository) {
//        System.out.println("🗝️ 시크릿 키 로딩됨 : " + secretKey);
//        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//        this.userRepository = userRepository;
//
//    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);
        String username = getUsername(accessToken);

        com.example.dulumi.domain.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        System.out.println("토큰에서 꺼낸 username : " + username);

        if (claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        String role = claims.get("role", String.class);

        // 클레임에서 권한 정보 가져오기
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        List<GrantedAuthority> authorities = List.of(authority);
//        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .toList();

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        System.out.println("🔒 validateToken secretKey : " + key);
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


    // accessToken
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public static String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //jjwt 라이브러리 식 토큰 검증
    public String createAccessToken(com.example.dulumi.domain.User user) {
        System.out.println("🔒 createToken secretKey : " + key);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256) // 🔥 여기서 key는 생성자에서 만든 key
                .compact();
    }
    //auth0 라이브러리 식 토큰 검증
//    public static String createAccessToken(com.example.dulumi.domain.User user) {
//        return JWT.create()
//                .withSubject(user.getUsername())
//                .withClaim("id", user.getId())
//                .withClaim("username", user.getUsername())
//                .withClaim("role", user.getRole().name())
//                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
//                .sign(algorithm);
//    }

    public String createRefreshToken(com.example.dulumi.domain.User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14)) // 예: 2주
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }
}
