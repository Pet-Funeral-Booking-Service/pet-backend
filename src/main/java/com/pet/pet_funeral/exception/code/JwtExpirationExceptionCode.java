package com.pet.pet_funeral.exception.code;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtExpirationExceptionCode extends RuntimeException{
    public JwtExpirationExceptionCode(String message) {
        super(message);
    }
}
