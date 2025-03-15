package com.pet.pet_funeral.domain.user.service;

import com.pet.pet_funeral.domain.user.model.RefreshToken;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.RefreshTokenRepository;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.redis.service.RedisService;
import com.pet.pet_funeral.security.jwt.JwtService;
import com.pet.pet_funeral.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshKeyExpiration;


    // db,redis 에 토큰 저장
    @Transactional
    public void save(UUID id, String refreshToken) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(id),"존재하지 않는 사용자입니다.");
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(refreshKeyExpiration);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user)
                        .token(refreshToken)
                        .createdAt(LocalDateTime.now())
                        .expiredAt(expiredAt)
                        .build()
        );
        redisService.saveRefreshToken(id, refreshToken);
    }

}
