package com.example.dulumi.service;



import com.example.dulumi.DTO.AddUserRequest;
import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.domain.Role;
import com.example.dulumi.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다." + username));

        log.info("유저를 찾았어여" + user);

        return (UserDetails) User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(Role.USER)
                .build();
    }

//    public void save(AddUserRequest request) {
//        User user = User.builder()
//                .username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .email(request.getEmail())
//                .role(Role.USER) // 기본 역할
//                .build();
//
//        log.info("User 저장 시도: {}", user); // ✅ 여기 로그 찍기
//        userRepository.save(user); // ✅ 여기서만 User 저장
//    }

    public User signup(AddUserRequest request) {
        System.out.println("🔥 Username: " + request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }


        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER) // 기본 역할
                .create_date(LocalDateTime.now())
                .provider("email")
                .providerId(request.getProvider_id())
                .build();

        return userRepository.save(user);
    }

    public User login(AddUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }
        return user;
    }
}
