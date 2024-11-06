package com.example.jwtformlogin.domain.jwt.controller;

import com.example.jwtformlogin.domain.jwt.enums.TokenType;
import com.example.jwtformlogin.domain.jwt.service.ReissueService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReissueController {

    private static final Logger log = LoggerFactory.getLogger(ReissueController.class);
    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        log.info("ReissueController : reissue");

        // refresh token 검증
        String refresh =  reissueService.verifyRefresh(request);
        String newAccess = reissueService.reissueAccess(refresh);
        Cookie newRefresh = reissueService.reissueRefresh(refresh);

        response.addHeader(TokenType.ACCESS.getHeader(), newAccess);
        response.addCookie(newRefresh);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
