package com.example.jwtformlogin.domain.jwt.error;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;

public class ExpiredRefreshTokenException extends ExpiredJwtException {
    public ExpiredRefreshTokenException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }

    public ExpiredRefreshTokenException(Header header, Claims claims, String message, Throwable cause) {
        super(header, claims, message, cause);
    }

    public ExpiredRefreshTokenException(ExpiredJwtException e){
        super(e.getHeader(), e.getClaims(), e.getMessage(), e);
    }

}
