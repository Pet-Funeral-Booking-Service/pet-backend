package com.pet.pet_funeral.exception.code;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestExceptionCode extends RuntimeException{
    public BadRequestExceptionCode(String message) {
        super(message);
    }
}
