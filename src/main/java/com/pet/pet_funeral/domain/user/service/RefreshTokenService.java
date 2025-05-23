package com.pet.pet_funeral.domain.user.service;

import com.pet.pet_funeral.domain.user.model.RefreshToken;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.RefreshTokenRepository;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.infra.redis.service.RedisService;
import com.pet.pet_funeral.security.jwt.JwtService;
import com.pet.pet_funeral.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * todo
 * RefreshTokenService 는  domain/user 보다는 security 폴더에 있는거 더 역할이 잘 분리될 듯 보임
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final RedisService redisService;

    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshKeyExpiration;


    // db,redis 에 토큰 저장
    @Transactional
    public void updateRefreshToken(UUID id, String refreshToken) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(id),"존재하지 않는 사용자입니다.");

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUser(user)
                .orElseGet(() -> refreshTokenRepository.save(
                        RefreshToken.builder()
                                .user(user)
                                .token(refreshToken)
                                .createdAt(LocalDateTime.now())
                                .expiredAt(LocalDateTime.now().plusSeconds(refreshKeyExpiration))
                                .build()
                ));
        refreshTokenEntity.updateRefreshToken(refreshToken,refreshKeyExpiration);
        redisService.updateRefreshToken(id, refreshToken);
    }

}
