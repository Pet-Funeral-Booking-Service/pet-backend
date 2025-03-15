package com.pet.pet_funeral.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
        return Optional.ofNullable(redisTemplate.opsForValue().get("refresh:" + id));
    }
    // 토큰 재발급
    public void updateRefreshToken(UUID id, String newRefreshToken) {
        // 기존 토큰 삭제 후 새 토큰 저장
        redisTemplate.delete("refresh:" + id);
        redisTemplate.opsForValue().set("refresh:" + id, newRefreshToken, refreshKeyExpiration, TimeUnit.SECONDS);
    }
    // 사용자 UUID 추출 (이메일 대신 UUID 기준으로)
    public Optional<UUID> getUserIdByRefreshToken(String refreshToken) {
        Set<String> keys = redisTemplate.keys("refresh:*");

        if (keys != null) {
            for (String key : keys) {
                String storedToken = redisTemplate.opsForValue().get(key);
                if (refreshToken.equals(storedToken)) {
                    // "refresh:{UUID}" -> "{UUID}"
                    String uuidString = key.replace("refresh:", "");
                    return Optional.of(UUID.fromString(uuidString));
                }
            }
        }

        return Optional.empty();
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
