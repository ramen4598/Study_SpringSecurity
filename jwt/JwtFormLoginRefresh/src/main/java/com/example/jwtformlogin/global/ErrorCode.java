package com.example.jwtformlogin.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "A001"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.", "A002"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다.", "A003"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다.", "A004"),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST,"이미 존재하는 username입니다", "B001"),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.", "B002"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Refresh token이 만료되었습니다.", "C001"),
    WRONG_CATEGORY_JWT(HttpStatus.BAD_REQUEST, "Wrong Category JWT", "C002"),
    NOT_EXIST_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "존재하지 않는 Refresh Token입니다.", "C003"),
    NO_REFRESH_TOKEN_COOKIE(HttpStatus.BAD_REQUEST, "Refresh Token Cookie가 존재하지 않습니다.", "C004");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
