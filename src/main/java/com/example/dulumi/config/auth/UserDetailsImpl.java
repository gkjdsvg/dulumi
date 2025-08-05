package com.example.dulumi.config.auth;

import com.example.dulumi.domain.User;
import com.neovisionaries.ws.client.ProxySettings;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;
    private String email;
    private String provider;
    private String role;
    private User user;


    public UserDetailsImpl(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + user.getRole()); // 권한
    }

    @Override
    public String getUsername() { //로그인 아이디
        return username;
    }

    @Override
    public String getPassword() { //로그인 비번
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { //계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //계정 잠김 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//비밀 번호 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() { //계정 활성화 여부
        return true;
    }
}
