package com.example.jwtformlogin.domain.jwt.repository;

import com.example.jwtformlogin.domain.jwt.entity.RefreshToken;
import com.example.jwtformlogin.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByValue(String value);

    void deleteAllByUser(UserEntity userEntity);

    @Transactional
    void deleteByValue(String value);
}
