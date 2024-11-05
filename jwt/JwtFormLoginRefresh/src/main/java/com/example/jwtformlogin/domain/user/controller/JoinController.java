package com.example.jwtformlogin.domain.user.controller;

import com.example.jwtformlogin.domain.user.dto.JoinRequestDto;
import com.example.jwtformlogin.domain.user.service.JoinService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {

    private static final Logger log = LoggerFactory.getLogger(JoinController.class);
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody @Valid JoinRequestDto request) {
        log.info("JoinController.join request: {}", request);

        joinService.join(request);

        return ResponseEntity.ok().build();
    }
}
