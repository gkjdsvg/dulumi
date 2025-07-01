package com.example.dulumi.config;

import com.example.dulumi.config.auth.AuthSuccessHandler;
import com.example.dulumi.service.PrincipalOauthUserService;
import com.example.dulumi.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PrincipalOauthUserService principalOauthUserService;
    private final AuthSuccessHandler authSuccessHandler;
    private final UserService userService;

    public SecurityConfig(PrincipalOauthUserService principalOauthUserService, AuthSuccessHandler authSuccessHandler, UserService userService) {
        this.principalOauthUserService = principalOauthUserService;
        this.authSuccessHandler = authSuccessHandler;
        this.userService = userService;
    }

    @Bean
    public WebSecurityCustomizer configurer() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
     }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/**", "/oauth2/**", "/login/oauth2/**","/joinForm").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAuthority("MANAGER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )

                .formLogin((fromLogin) ->
                        fromLogin
                                .loginPage("/loginForm")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/main")
                )
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/loginForm")
                                .successHandler(authSuccessHandler)
                                .defaultSuccessUrl("/main")
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                        .userService(principalOauthUserService)//구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함 . Tip.코드x, (엑세스 토큰+사용자 프로필 정보를 받아옴)
                                )
                );

        return http.build();
    }
}
