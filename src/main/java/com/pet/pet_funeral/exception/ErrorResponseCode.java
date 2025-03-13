package com.pet.pet_funeral.exception;

/**
 * 에러 상태 코드 관리
 * 클라이언트 관련 에러 40xx
 * 서버 관련 에러 50xx
 */
public enum ErrorResponseCode {
    NOT_FOUND(4041);
    
    private final int code;

    ErrorResponseCode(int c) {
        this.code = c;
    }

    public int getCode() {
        return this.code;
    }
}