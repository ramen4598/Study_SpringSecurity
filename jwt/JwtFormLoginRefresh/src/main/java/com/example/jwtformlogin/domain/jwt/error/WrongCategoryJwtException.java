package com.example.jwtformlogin.domain.jwt.error;

public class WrongCategoryJwtException extends RuntimeException {
    public WrongCategoryJwtException() {
        super("Wrong Category JWT");
    }
}
