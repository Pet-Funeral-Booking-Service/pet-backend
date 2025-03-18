package com.pet.pet_funeral.security.dto;

import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.pet.pet_funeral.domain.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseCookie;


@Getter
@AllArgsConstructor
public class LoginResponse {
    private final Role role;
    private final String accessToken;
    private final ResponseCookie refreshTokenCookie;
}
