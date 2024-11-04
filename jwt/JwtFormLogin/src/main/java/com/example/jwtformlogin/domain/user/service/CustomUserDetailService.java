package com.example.jwtformlogin.domain.user.service;

import com.example.jwtformlogin.domain.user.dto.CustomUserDetails;
import com.example.jwtformlogin.domain.user.entity.UserEntity;
import com.example.jwtformlogin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// DB에서 사용자 정보를 UserDetails의 구현체에 담아서 가져오는 클래스
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(userEntity);
    }
}
