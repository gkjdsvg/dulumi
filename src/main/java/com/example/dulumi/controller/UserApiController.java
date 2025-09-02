package com.example.dulumi.controller;

import com.auth0.jwt.JWT;
import com.example.dulumi.DTO.AddUserRequest;
import com.example.dulumi.Repository.JwtRepository;
import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.config.JWT.*;
import com.example.dulumi.domain.PrincipalDetails;
import com.example.dulumi.domain.User;
import com.example.dulumi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
public class UserApiController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;

    public UserApiController(UserService userService, JWTUtil jwtUtil, JwtProvider jwtProvider, JwtService jwtService, UserRepository userRepository, JwtRepository jwtRepository) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.jwtRepository = jwtRepository;
    }

    @PostMapping("/loginForm")
    public ResponseEntity<String> login(@RequestBody AddUserRequest addUserRequest){
        System.out.println("🥝🥝🥝 컨트롤러 들어옴");
        userService.login(addUserRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/joinForm")
    public ResponseEntity<String> signup(@RequestBody AddUserRequest addUserRequest) {
        System.out.println("🔥🔥🔥🔥🔥 컨트롤러 들어옴");
        userService.signup(addUserRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/loginForm";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login-api")
    public ResponseEntity<Map<String, String>> loginForm(@RequestBody AddUserRequest addUserRequest) {
        System.out.println("✅ 로그인 요청 들어옴 : " + addUserRequest.getEmail());
        User user = userService.login(addUserRequest);
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        //저장
        jwtService.saveOrUpdateToken(user, accessToken, refreshToken);

        System.out.println("== 디버깅용 JWT ==");
        System.out.println("Access Token = " + accessToken);
        System.out.println("Decoded Role = " + JWT.decode(accessToken).getClaim("role").asString());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/signup-api")
    public ResponseEntity<JwtDto> signup_api(@RequestBody AddUserRequest request) {
        User user = userService.signup(request);

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        return  ResponseEntity.ok(new JwtDto("Bearer", accessToken, refreshToken));
    }

    @PostMapping("api/dev-token")
    public ResponseEntity<String> getDevToken(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Jwt jwt = jwtRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("토큰 없음"));

        return ResponseEntity.ok(jwt.getAccessToken());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "unauthenticated"));
        }
        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }
}
