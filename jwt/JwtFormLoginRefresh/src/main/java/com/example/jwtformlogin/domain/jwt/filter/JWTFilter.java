package com.example.jwtformlogin.domain.jwt.filter;

import com.example.jwtformlogin.domain.jwt.JWTUtil;
import com.example.jwtformlogin.domain.user.dto.CustomUserDetails;
import com.example.jwtformlogin.domain.user.entity.UserEntity;
import com.example.jwtformlogin.domain.user.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// 로그인 후 보내는 요청에 대한 JWT 검증 필터
// fiter chain에 추가할 JWT 검증 필터
// JWT 토큰을 검증하고 정상적인 토큰인 경우 SecurityContextHolder에 일시적인 세션을 생성
// 해당 세션은 Stateless 상태로 관리되어 해당 요청이 끝나면 사라짐
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        // token이 없거나 비어있는 경우
        if(token == null || token.isBlank()){
            log.info("Request without Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 만료되었는지 확인
        try{
            jwtUtil.isExpired(token);
        }catch (ExpiredJwtException e){
            log.error("JWTFilter : Access Token is Expired");

            // reponse body
            PrintWriter writer = response.getWriter();
            writer.println("Access Token is Expired");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // 토큰의 카테고리가 access인지 확인
        String category = jwtUtil.getCategory(token);
        if(!category.equals("access")){
            log.error("JWTFilter : Invalid Token Category");

            // reponse body
            PrintWriter writer = response.getWriter();
            writer.println("Invalid Token Category");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("temppassword") // 토큰에는 password가 없으므로 임시로 넣어줌
                .role(Role.valueOf(role)) // Enum 타입으로 변환
                .build();

        // 사용자 정보를 담은 CustomUserDetails 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        // 스프링 시큐리티 인증 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        // SecurityContextHolder 세션에 인증 정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
