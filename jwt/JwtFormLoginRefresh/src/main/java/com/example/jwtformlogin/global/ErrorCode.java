package com.example.jwtformlogin.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request", "A001"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized", "A002"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden", "A003"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found", "A004"),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST,"Duplicate username", "B001"),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "Not exist user", "B002"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Access token is expired", "C001"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Refresh token is expired", "C002"),
    WRONG_CATEGORY_JWT(HttpStatus.BAD_REQUEST, "Wrong Category JWT", "C003"),
    NOT_EXIST_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Not exist refresh token", "C004"),
    NO_REFRESH_TOKEN_COOKIE(HttpStatus.BAD_REQUEST, "No refresh token cookie", "C005");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
