package com.pet.pet_funeral.domain.user.service;

import com.pet.pet_funeral.domain.user.model.Role;
import com.pet.pet_funeral.domain.user.model.LoginType;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.domain.user.dto.KakaoTokenResponse;
import com.pet.pet_funeral.domain.user.dto.KakaoUserResponse;
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
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client_id}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final RefreshTokenService refreshTokenService;
    private final RedisService redisService;

    @Transactional
    public LoginResponse kakaoLogin(String code){
        String kakaoToken = getKakaoToken(code);
        log.info("kakaoAccessToken: {}", kakaoToken);

        String kakaoId = String.valueOf(getKakaoUser(kakaoToken));
        log.info("kakaoId: {}", kakaoId);

        User user = userRepository.findBySocialId(kakaoId)
                .orElseGet(() -> registerUser(kakaoId));

        String accessToken = jwtService.createAccessToken(new AccessTokenPayload(user.getId(),user.getRole(),new Date()));
        String refreshToken = jwtService.createRefreshToken(new RefreshTokenPayload(user.getId(),new Date()));
        ResponseCookie responseCookie = cookieService.createRefreshTokenCookie(refreshToken);
        // 로그인하고 이미 DB 에 저장된 유저가 다시 로그인할 때 리프레시값이 중복되지 않으려면 수정 로직 필요
        refreshTokenService.updateRefreshToken(user.getId(),refreshToken);
        return new LoginResponse(Role.USER,accessToken,responseCookie);
    }

    public String getKakaoToken(String code){
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
                KAKAO_TOKEN_URL,
                request,
                KakaoTokenResponse.class
        );
        return response.getBody().getAccessToken();
    }
    private Long getKakaoUser(String accessToken){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                KAKAO_USER_URL,
                HttpMethod.GET,
                request,
                KakaoUserResponse.class
        );
        return response.getBody().getId();
    }
    private User registerUser(String kakaoId){
        User user = User.builder()
                .socialId(kakaoId)
                .loginType(LoginType.KAKAO)
                .role(Role.USER)
                .build();

        log.info("registerUser: {}", user);
        return userRepository.save(user);
    }
}
