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
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Refresh token이 만료되었습니다.", "C001"),
    WRONG_CATEGORY_JWT(HttpStatus.BAD_REQUEST, "Wrong Category JWT", "C002");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
