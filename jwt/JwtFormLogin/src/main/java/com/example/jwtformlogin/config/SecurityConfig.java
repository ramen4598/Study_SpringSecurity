package com.example.jwtformlogin.config;

import com.example.jwtformlogin.domain.jwt.JWTUtil;
import com.example.jwtformlogin.domain.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((auth) -> auth
                        // /login, /join, / 경로로 들어오는 요청은 인증이 필요하지 않음
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/join"),
                                new AntPathRequestMatcher("/")
                        ).permitAll()
                        // /admin 경로로 들어오는 요청은 ADMIN 권한이 필요
                        .requestMatchers(
                                new AntPathRequestMatcher("/admin")
                        ).hasRole("ADMIN")
                        // 나머지 요청은 인증이 필요
                        .anyRequest().authenticated()
                )

                //  jwt token에서는 csrf 설정을 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // form login, http basic 설정을 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 커스텀 login filter를 UsernamePasswordAuthenticationFilter 대신에 추가
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)

                //.logout(AbstractHttpConfigurer::disable) // logout 설정을 비활성화

                // jwt token에서는 session 설정을 비활성화
                .sessionManagement((management) -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // /api/** 경로로 들어오는 요청에 대해 인증되지 않은 경우 401 UNAUTHORIZED 응답을 반환
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
                )
                .build();
    }

    // 외부에서 AuthenticatinConfiguration을 주입받아서 AuthenticationManager를 생성해서 반환
    // AuthenticationManager는 인증을 처리하는 인터페이스
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
