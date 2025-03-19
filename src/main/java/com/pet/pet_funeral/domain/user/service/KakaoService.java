package com.pet.pet_funeral.domain.user.service;

import com.pet.pet_funeral.domain.user.model.Role;
import com.pet.pet_funeral.domain.user.model.LoginType;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.domain.user.dto.KakaoTokenResponse;
import com.pet.pet_funeral.domain.user.dto.KakaoUserResponse;
import com.pet.pet_funeral.domain.user.service.impl.SocialLoginServiceImpl;
import com.pet.pet_funeral.infra.redis.service.RedisService;
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
public class KakaoService extends SocialLoginServiceImpl {

    @Value("${kakao.client_id}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.user_url}")
    private String kakaoUserUrl;

    @Value("${kakao.token_url")
    private String kakaoTokenUrl;


    public KakaoService(UserRepository userRepository, JwtService jwtService, CookieService cookieService, RefreshTokenService refreshTokenService) {
        super(userRepository, jwtService, cookieService, refreshTokenService);
    }


    @Override
    public LoginType getLoginType() {
        return LoginType.KAKAO;
    }

    public String getToken(String code){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                kakaoTokenUrl,
                request,
                KakaoTokenResponse.class
        );
        return response.getBody().getAccessToken();
    }
    protected String getUser(String accessToken){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                kakaoUserUrl,
                HttpMethod.GET,
                request,
                KakaoUserResponse.class
        );
        return String.valueOf(response.getBody().getId());
        // 카카오에서 반환받은 Long 타입을 백엔드에서 String 으로 변환
    }

}
