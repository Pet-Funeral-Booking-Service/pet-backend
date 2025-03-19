package com.pet.pet_funeral.exception.code;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtValidationExceptionCode extends RuntimeException {
    public JwtValidationExceptionCode(String message) {
        super(message);
    }
}
 // 토큰 존재 유무, 토큰 비교 검증