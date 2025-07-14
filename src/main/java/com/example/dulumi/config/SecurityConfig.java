package com.example.dulumi.config;

import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.config.JWT.JwtAuthenticationFilter;
import com.example.dulumi.config.JWT.JwtProvider;
import com.example.dulumi.config.auth.AuthSuccessHandler;
import com.example.dulumi.service.PrincipalOauthUserService;
import com.example.dulumi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PrincipalOauthUserService principalOauthUserService;
    private final AuthSuccessHandler authSuccessHandler;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public SecurityConfig(PrincipalOauthUserService principalOauthUserService, AuthSuccessHandler authSuccessHandler, JwtProvider jwtProvider, UserRepository userRepository) {
        this.principalOauthUserService = principalOauthUserService;
        this.authSuccessHandler = authSuccessHandler;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
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
                        .requestMatchers("/loginForm", "/oauth2/**", "/login/oauth2/**","/joinForm","/signup-api","login-api").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAuthority("MANAGER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/notification/subscribe").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(userRepository, jwtProvider), UsernamePasswordAuthenticationFilter.class)

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
                )
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint((request, response, authExcpetion) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }));

        return http.build();
    }
}
