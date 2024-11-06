package com.example.jwtformlogin.domain.jwt.controller;

import com.example.jwtformlogin.domain.jwt.service.ReissueService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReissueController {

    private static final Logger log = LoggerFactory.getLogger(ReissueController.class);
    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(value = "refresh") String refresh) {

        log.info("ReissueController : reissue");

        String newAccess = reissueService.reissue(refresh);
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", newAccess).build();
    }
}
