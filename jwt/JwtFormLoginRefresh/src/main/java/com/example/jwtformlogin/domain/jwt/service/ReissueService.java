package com.example.jwtformlogin.domain.jwt.service;

import com.example.jwtformlogin.domain.jwt.JWTUtil;
import com.example.jwtformlogin.domain.jwt.error.ExpiredRefreshTokenException;
import com.example.jwtformlogin.domain.jwt.error.WrongCategoryJwtException;
import io.jsonwebtoken.ExpiredJwtException;
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

    private static final Logger log = LoggerFactory.getLogger(ReissueService.class);

    private final JWTUtil jwtUtil;

    public String reissue(String refresh) {

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

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // make new access token
        return jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_EXPIRE_TIME);
    }
}
