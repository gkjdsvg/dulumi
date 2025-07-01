package com.example.dulumi.controller;

import com.example.dulumi.DTO.AddUserRequest;
import com.example.dulumi.DTO.jwtDto;
import com.example.dulumi.config.JWT.CreateJwt;
import com.example.dulumi.config.JWT.JWTUtil;
import com.example.dulumi.domain.User;
import com.example.dulumi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
public class UserApiController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserApiController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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

    @PostMapping("/login-api")
    public ResponseEntity<jwtDto> loginForm(@RequestBody AddUserRequest addUserRequest) {
        User user = userService.login(addUserRequest);

        jwtDto token = jwtUtil.generateToken(user.getId());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup-api")
    public ResponseEntity<jwtDto> signup_api(@RequestBody AddUserRequest request) {
        User user = userService.signup(request);

        String accessToken = CreateJwt.createAccessToken(user);
        String refreshToken = CreateJwt.createRefreshToken(user, accessToken);

        return  ResponseEntity.ok(new jwtDto("Bearer", accessToken, refreshToken));
    }
}
