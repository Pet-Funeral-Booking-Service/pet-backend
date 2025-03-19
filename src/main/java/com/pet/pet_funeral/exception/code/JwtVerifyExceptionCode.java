package com.pet.pet_funeral.exception.code;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtVerifyExceptionCode extends RuntimeException {
    public JwtVerifyExceptionCode(String message) {
        super(message);
    }
}
