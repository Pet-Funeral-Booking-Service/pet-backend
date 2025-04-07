package com.pet.pet_funeral.infra.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * todo
 * RedisService 보다 JwtCacheService 이런식으로 네이밍 한 뒤 security/service 경로에 넣으면 좋을 것 같음
 * 나중에 infra 모듈을 따로 분리하게 될 경우 RedisService 가 토큰 관련 기능을 담당하고 있어 역할 분리가 힘들 것 같음
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh.expiration}")
    private long refreshKeyExpiration;

    // 토큰 저장
    public void saveRefreshToken(UUID id, String refreshToken){
        redisTemplate.opsForValue().set("refresh : " + id, refreshToken,refreshKeyExpiration, TimeUnit.SECONDS);
    }
    // 토큰 조회
    public Optional<String> getRefreshToken(UUID id){
        return Optional.ofNullable(redisTemplate.opsForValue().get("refresh : " + id));
    }
    // 토큰 재발급
    public void updateRefreshToken(UUID id, String newRefreshToken) {
        deleteRefreshToken(id);
        saveRefreshToken(id, newRefreshToken);
    }
    // 사용자 UUID 추출 (이메일 대신 UUID 기준으로)
    public Optional<UUID> getUserIdByRefreshToken(String refreshToken) {

        Set<String> keys = redisTemplate.keys("refresh:*");
        if (keys == null || keys.isEmpty()) return Optional.empty();

        return keys.stream()
                .filter(key -> refreshToken.equals(redisTemplate.opsForValue().get(key)))
                .map(key -> key.replace("refresh:", "")) // refresh 를 지우고 뒤에 UUID 를 가져옴
                .map(UUID::fromString)
                .findFirst();
    }

    // 토큰 삭제
    public void deleteRefreshToken(UUID id){
        redisTemplate.delete(String.valueOf(id));
    }
    // 토큰 유효성 검증
    public boolean isRefreshTokenValid(UUID id,String refreshToken) throws Exception{
        return getRefreshToken(id).equals(refreshToken);
    }
}
