package com.pet.pet_funeral.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private boolean success;
    private String message;
    private T data;
}