package com.example.jwtformlogin.domain.user.entity;

import com.example.jwtformlogin.domain.user.dto.JoinRequestDto;
import com.example.jwtformlogin.domain.user.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 25)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

    @Builder
    public UserEntity(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static UserEntity of(JoinRequestDto request) {
        return UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(Role.ROLE_USER)
                .build();
    }
}
