package com.example.jwtformlogin.domain.main.controller;

import com.example.jwtformlogin.domain.user.dto.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String mainP() {
        log.info("MainController mainP");

        return "Hello MainController";
    }

    @GetMapping("/userinfo")
    public String userinfo(){
        log.info("MainController userinfo");

        StringBuilder sb = new StringBuilder();
        sb.append("User Info\n");
        sb.append("Username: ");
        sb.append(SecurityContextHolder.getContext().getAuthentication().getName());
        sb.append("\n");
        sb.append("Role: ");
        sb.append(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        sb.append("\n");

        return sb.toString();
    }
}
