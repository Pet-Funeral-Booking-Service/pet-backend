package com.pet.pet_funeral.security.service;

import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.infra.redis.service.RedisService;
import com.pet.pet_funeral.security.dto.AccessTokenPayload;
import com.pet.pet_funeral.security.dto.LoginResponse;
import com.pet.pet_funeral.security.dto.RefreshTokenPayload;
import com.pet.pet_funeral.security.jwt.JwtService;
import com.pet.pet_funeral.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final CookieService cookieService;

    @Value("${jwt.access.expiration}")
    private long accessKeyExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshKeyExpiration;

    @Transactional
    public LoginResponse login(UUID id){
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(id),"존재하지 않는 이메일입니다.");
        // 로그인 이후에는 DB 에서 가져오는 게 아닌 Authentication 이나 UserDetails 에서 가져오는게 더 효율적임
        String refreshToken = jwtService.createRefreshToken(new RefreshTokenPayload(user.getId(),new Date()));
        redisService.updateRefreshToken(id,refreshToken);

        String accessToken = jwtService.createAccessToken(new AccessTokenPayload(user.getId(),user.getRole(),new Date()));

        ResponseCookie responseCookie = cookieService.createRefreshTokenCookie(refreshToken);

        return new LoginResponse(user.getRole(),accessToken,responseCookie);

    }


}
