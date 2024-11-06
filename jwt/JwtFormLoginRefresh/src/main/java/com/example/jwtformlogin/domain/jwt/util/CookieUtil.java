package com.example.jwtformlogin.domain.jwt.util;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${spring.jwt.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;
    @Value("${spring.jwt.refresh.cookie.path}")
    private String REFRESH_TOKEN_COOKIE_PATH;

    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(REFRESH_TOKEN_EXPIRE_TIME.intValue());
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        return cookie;
    }
}
