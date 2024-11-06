package com.example.jwtformlogin.config;

import com.example.jwtformlogin.domain.jwt.CustomAuthenticationEntryPoint;
import com.example.jwtformlogin.domain.jwt.filter.JWTFilter;
import com.example.jwtformlogin.domain.jwt.util.CookieUtil;
import com.example.jwtformlogin.domain.jwt.util.JWTUtil;
import com.example.jwtformlogin.domain.jwt.filter.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${cors.url}")
    private String corsURL;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] allowedOrigins = Arrays.stream(corsURL.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        return http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true); // 토큰을 주고받을 때는 credentials를 허용해야 함
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L); // 1시간동안 캐싱

                                // 클라이언트에서 Authorization 헤더를 사용할 수 있도록 설정
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        })
                )
                .authorizeHttpRequests((auth) -> auth
                        // /login, /join, / 경로로 들어오는 요청은 인증이 필요하지 않음
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/join"),
                                new AntPathRequestMatcher("/reissue"),
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

                // form login, http basic, logout 설정을 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                //.logout(AbstractHttpConfigurer::disable) // logout 설정을 비활성화

                // jwt token 유효성 검사를 위한 필터 추가
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                // login : 커스텀 login filter를 UsernamePasswordAuthenticationFilter 대신에 추가
                .addFilterAt(LoginFilter.builder()
                                .authenticationManager(authenticationConfiguration.getAuthenticationManager())
                                .jwtUtil(jwtUtil)
                                .cookieUtil(cookieUtil)
                                .build()
                        , UsernamePasswordAuthenticationFilter.class)

                // jwt token에서는 session 설정을 비활성화
                .sessionManagement((management) -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // /api/** 경로로 들어오는 요청에 대해 인증되지 않은 경우 401 UNAUTHORIZED 응답을 반환
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                //new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new CustomAuthenticationEntryPoint(HttpStatus.UNAUTHORIZED), // log를 찍기 위해 커스텀한 EntryPoint
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
