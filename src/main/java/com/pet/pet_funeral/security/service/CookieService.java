package com.pet.pet_funeral.security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CookieService {


    @Value("${jwt.refresh.expiration}")
    private long refreshKeyExpiration;

    private final String REFRESH_TOKEN = "refreshToken";

    public Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN)) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }


    public ResponseCookie createRefreshTokenCookie(final String refreshToken) {
        return ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true) // 자바스크립트에서 접근 불가
                .secure(false) // https 에서만 허용(운영에서는 true)
                .maxAge(refreshKeyExpiration) // 쿠키 유효시
                .sameSite("Strict") // CSRF 공격 방지
                .path("/")
                .build();
    }
}
