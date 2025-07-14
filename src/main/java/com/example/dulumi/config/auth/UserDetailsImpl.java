package com.example.dulumi.config.auth;

import com.example.dulumi.domain.User;
import com.neovisionaries.ws.client.ProxySettings;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

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

    @Builder
    public UserDetailsImpl(String email, String password, String provider, String username, String role, User user) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return getRole().toString();
            }
        });
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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
}
