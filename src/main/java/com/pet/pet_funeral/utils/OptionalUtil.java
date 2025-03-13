package com.pet.pet_funeral.utils;


import com.pet.pet_funeral.exception.code.NotFoundDataExceptionCode;

import java.util.Optional;

public class OptionalUtil {
    public static <T> T getOrElseThrow(final Optional<T> optional, final String message) {
        return optional.orElseThrow(() -> new NotFoundDataExceptionCode(message));
    }
}
