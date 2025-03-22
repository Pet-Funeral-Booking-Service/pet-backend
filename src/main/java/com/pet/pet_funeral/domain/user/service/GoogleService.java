package com.pet.pet_funeral.domain.user.service;
import com.pet.pet_funeral.domain.user.dto.GoogleTokenResponse;
import com.pet.pet_funeral.domain.user.dto.GoogleUserResponse;
import com.pet.pet_funeral.domain.user.dto.KakaoTokenResponse;
import com.pet.pet_funeral.domain.user.model.LoginType;
import com.pet.pet_funeral.domain.user.model.Role;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.domain.user.service.impl.SocialLoginService;
import com.pet.pet_funeral.domain.user.service.impl.SocialLoginServiceImpl;
import com.pet.pet_funeral.security.dto.AccessTokenPayload;
import com.pet.pet_funeral.security.dto.LoginResponse;
import com.pet.pet_funeral.security.dto.RefreshTokenPayload;
import com.pet.pet_funeral.security.jwt.JwtService;
import com.pet.pet_funeral.security.service.CookieService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Slf4j
@Service
public class GoogleService extends SocialLoginServiceImpl {

    @Value("${google.client_id}")
    private String googleApiKey;

    @Value("${google.client_secret}")
    private String googleSecret;

    @Value("${google.redirect_uri}")
    private String googleRedirectUrl;

    @Value("${google.user_url}")
    private String googleUserUrl;

    @Value("${google.token_url}")
    private String googleTokenUrl;

    public GoogleService(UserRepository userRepository, JwtService jwtService, CookieService cookieService, RefreshTokenService refreshTokenService) {
        super(userRepository, jwtService, cookieService, refreshTokenService);
    }


    @Override
    public LoginType getLoginType() {
        return LoginType.GOOGLE;
    }

    public String getToken(String code){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + code);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleApiKey);
        params.add("client_secret", googleSecret);
        params.add("redirect_uri", googleRedirectUrl);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
                googleTokenUrl, request, GoogleTokenResponse.class);

        return response.getBody().getAccessToken();
    }
    public String getUser(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserResponse> response = restTemplate.exchange(
                googleUserUrl, HttpMethod.GET,request,GoogleUserResponse.class
        );
        return response.getBody().getId();
    }



}
