package com.pet.pet_funeral.exception;

import com.pet.pet_funeral.exception.code.*;
import com.pet.pet_funeral.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(BadRequestExceptionCode.class)
    public ResponseEntity<SuccessResponse<String>> handleBadRequest(BadRequestExceptionCode e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new SuccessResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(ExistValueExceptionCode.class)
    public ResponseEntity<SuccessResponse<String>> handleExistValue(ExistValueExceptionCode e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new SuccessResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(NotFoundDataExceptionCode.class)
    public ResponseEntity<SuccessResponse<String>> handleNotFoundData(NotFoundDataExceptionCode e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new SuccessResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessResponse<String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SuccessResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(JwtVerifyExceptionCode.class)
    public ResponseEntity<SuccessResponse<String>> handleJwtVerifyException(JwtVerifyExceptionCode e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new SuccessResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(JwtExpirationExceptionCode.class)
    public ResponseEntity<SuccessResponse<String>> handleJwtExpirationException(JwtExpirationExceptionCode e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new SuccessResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(JwtValidationExceptionCode.class)
    public ResponseEntity<SuccessResponse<String>> handleJwtValidationException(JwtValidationExceptionCode e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new SuccessResponse<>(false, e.getMessage(), null));
    }
}
