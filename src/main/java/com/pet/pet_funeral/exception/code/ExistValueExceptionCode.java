package com.pet.pet_funeral.exception.code;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExistValueExceptionCode extends RuntimeException {
    public ExistValueExceptionCode(String message) {
        super(message);
    }
}
