package com.example.jwtformlogin.global;

import com.example.jwtformlogin.domain.user.error.DuplicateUsername;
import lombok.Getter;
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

    private ResponseEntity<ErrorResponseDto> getResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponseDto(errorCode));
    }
}
