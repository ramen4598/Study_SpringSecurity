package com.example.jwtformlogin.domain.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private final String REFRESH_TOKEN_COOKIE_PATH;
    private final Long ACCESS_TOKEN_EXPIRE_TIME;
    private final Long REFRESH_TOKEN_EXPIRE_TIME;

    @Builder
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       String REFRESH_TOKEN_COOKIE_PATH, Long ACCESS_TOKEN_EXPIRE_TIME,
                       Long REFRESH_TOKEN_EXPIRE_TIME) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.REFRESH_TOKEN_COOKIE_PATH = REFRESH_TOKEN_COOKIE_PATH;
        this.ACCESS_TOKEN_EXPIRE_TIME = ACCESS_TOKEN_EXPIRE_TIME;
        this.REFRESH_TOKEN_EXPIRE_TIME = REFRESH_TOKEN_EXPIRE_TIME;
    }

    // username, password를 받아서 인증
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

       // username, password를 받아서 토큰을 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // 검증
        return authenticationManager.authenticate(authToken);
    }

    // 인증이 성공하면 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) {
        log.info("login success");

        // 유정 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_EXPIRE_TIME); // access token
        String refresh = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_EXPIRE_TIME);

        // 토큰을 헤더에 담아서 반환
        response.setHeader("Authorization", access); // access token
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    // 인증이 실패하면 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("login fail");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(REFRESH_TOKEN_EXPIRE_TIME.intValue());
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        return cookie;
    }

}
