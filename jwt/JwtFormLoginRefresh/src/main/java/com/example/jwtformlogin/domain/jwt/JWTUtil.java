package com.example.jwtformlogin.domain.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private final SecretKey SECRET_KEY;

    public JWTUtil(@Value("${spring.jwt.secret}") String secretKey) {
        // SecretKey 생성
        // SecretKeySpec 생성자는 바이트 배열과 알고리즘 이름을 사용하여 SecretKey 객체를 생성.
        // SecretKey는 JWT 토큰의 서명과 검증에 사용됩니다.
        SECRET_KEY = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build() // SECRET_KEY로 토큰을 검증하는 파서 생성
                .parseSignedClaims(token).getPayload().get("username", String.class); // 토큰을 파싱하여 username을 반환
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build() // SECRET_KEY로 토큰을 검증하는 파서 생성
                .parseSignedClaims(token).getPayload().get("role", String.class); // 토큰을 파싱하여 role을 반환
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build() // SECRET_KEY로 토큰을 검증하는 파서 생성
                .parseSignedClaims(token).getPayload().getExpiration().before(new Date()); // 토큰의 만료 시간을 반환
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build() // SECRET_KEY로 토큰을 검증하는 파서 생성
                .parseSignedClaims(token).getPayload().get("category", String.class); // 토큰을 파싱하여 category를 반환
    }

    public String createJwt(String category, String username, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category) // access or refresh
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SECRET_KEY)
                .compact(); // 토큰 생성
    }

}
