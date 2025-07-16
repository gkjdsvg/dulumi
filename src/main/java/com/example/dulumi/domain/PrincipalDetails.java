package com.example.dulumi.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User{
    private final User user;
    private Map<String, Object> attributes;


    //일반 로그인 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth 로그인 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes ) {
        this.user = user;
    }

    /**
     * OAuth2User 인터페이스 메소드
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    /**
     * UserDetails 인터페이스 메소드
     */
    // 해당 User의 권한을 리턴하는 곳!(role)
    // SecurityFilterChain에서 권한을 체크할 때 사용됨
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
