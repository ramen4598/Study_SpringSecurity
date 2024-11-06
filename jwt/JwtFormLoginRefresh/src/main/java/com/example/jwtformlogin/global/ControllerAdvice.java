package com.example.jwtformlogin.global;

import com.example.jwtformlogin.domain.jwt.error.ExpiredRefreshTokenException;
import com.example.jwtformlogin.domain.jwt.error.WrongCategoryJwtException;
import com.example.jwtformlogin.domain.user.error.DuplicateUsername;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DuplicateUsername.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateUsername(DuplicateUsername e) {
        log.error("handleDuplicateUsername", e);
        return getResponse(ErrorCode.DUPLICATE_USERNAME);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleExpiredRefreshTokenException(ExpiredRefreshTokenException e) {
        log.error("handleExpiredRefreshTokenException", e);
        return getResponse(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    @ExceptionHandler(WrongCategoryJwtException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongCategoryJwtException(WrongCategoryJwtException e) {
        log.error("handleWrongCategoryJwtException", e);
        return getResponse(ErrorCode.WRONG_CATEGORY_JWT);
    }

    private ResponseEntity<ErrorResponseDto> getResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponseDto(errorCode));
    }
}
