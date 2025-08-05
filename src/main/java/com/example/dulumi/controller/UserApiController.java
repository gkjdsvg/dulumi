package com.example.dulumi.controller;

import com.auth0.jwt.JWT;
import com.example.dulumi.DTO.AddUserRequest;
import com.example.dulumi.DTO.JwtDto;
import com.example.dulumi.config.JWT.JWTUtil;
import com.example.dulumi.config.JWT.JwtProvider;
import com.example.dulumi.domain.User;
import com.example.dulumi.service.JwtService;
import com.example.dulumi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
public class UserApiController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    public UserApiController(UserService userService, JWTUtil jwtUtil, JwtProvider jwtProvider, JwtService jwtService) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.jwtService = jwtService;
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

    @GetMapping("/user/info")
    public ResponseEntity<String> getUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("hello" + user.getUsername());
    }
}
