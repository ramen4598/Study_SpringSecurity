package com.example.jwtformlogin.domain.user.service;

import com.example.jwtformlogin.domain.user.dto.JoinRequestDto;
import com.example.jwtformlogin.domain.user.entity.UserEntity;
import com.example.jwtformlogin.domain.user.error.DuplicateUsername;
import com.example.jwtformlogin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {

    private static final Logger log = LoggerFactory.getLogger(JoinService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinRequestDto request) {

        validateDuplicateUsername(request.getUsername()); // 중복되는 username이 있는지 확인하는 로직

       encodePassword(request); // password를 암호화하는 로직

        userRepository.save(UserEntity.of(request));
    }

    private void validateDuplicateUsername(String username) {
        if(userRepository.existsByUsername(username)) {
            throw new DuplicateUsername(username);
        }
    }

    private void encodePassword(JoinRequestDto request) {
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        request.encodePassword(encodedPassword);
    }
}
