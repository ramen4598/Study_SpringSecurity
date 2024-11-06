package com.example.jwtformlogin.domain.jwt.entity;

import com.example.jwtformlogin.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user 당 refresh token은 1개라면 unique = true
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "expiration", nullable = false)
    private String expiration;

    @Builder
    public RefreshToken(UserEntity user, String value, String expiration) {
        this.user = user;
        this.user.updateRefreshToken(this);
        this.value = value;
        this.expiration = expiration;
    }
}
