package com.example.jwtformlogin.domain.jwt.service;

import com.example.jwtformlogin.domain.jwt.util.CookieUtil;
import com.example.jwtformlogin.domain.jwt.util.JWTUtil;
import com.example.jwtformlogin.domain.jwt.error.ExpiredRefreshTokenException;
import com.example.jwtformlogin.domain.jwt.error.WrongCategoryJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReissueService {

    @Value("${spring.jwt.access.expiration}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${spring.jwt.access.expiration}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    private static final Logger log = LoggerFactory.getLogger(ReissueService.class);

    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public void verifyRefresh(String refresh) {

        // refresh token 검증
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException(e);
        }

        // category 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            log.error("ReissueController : reissue : category is not refresh");
            throw new WrongCategoryJwtException();
        }
    }

    public String reissueAccess(String refresh) {

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // make new access token
        return jwtUtil.createJwt("access", username, role);
    }

    public Cookie reissueRefresh(String refresh) {

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // make new refresh token
        String newRefresh = jwtUtil.createJwt("refresh", username, role);
        // TODO : save

        return cookieUtil.createCookie("refresh", newRefresh);
    }
}
