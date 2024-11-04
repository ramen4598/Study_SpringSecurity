package com.example.jwtformlogin.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "U001"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.", "U002"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다.", "U003"),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST,"이미 존재하는 username입니다", "U004"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다.", "R001");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
