package com.pet.pet_funeral.domain.pet_funeral.service;

import com.pet.pet_funeral.domain.user.model.Role;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.security.dto.AccessTokenPayload;
import com.pet.pet_funeral.security.dto.RefreshTokenPayload;
import com.pet.pet_funeral.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        String secretKey = Base64.getEncoder().encodeToString("5ukZB4aZH+6twWqHJrTRZkS6VYcSL2HeKTDWQAtXs9NYtyqBJSHXZ3MT6d9Pj/JdA+RrNRPabmWJkxYuHtrGlQ==".getBytes());
        jwtService = new JwtService(secretKey);
    }

    @Test
    @DisplayName("엑세스 토큰 생성 테스트")
    void 엑세스토큰_생성() throws Exception {
        //given
        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID(), Role.USER, new Date());

        //when
        String token = jwtService.createAccessToken(payload);

        //then
        Assertions.assertNotNull(token);
    }
    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    void 리프레시토큰_생성() throws Exception {
        //given
        RefreshTokenPayload payload = new RefreshTokenPayload(UUID.randomUUID(), new Date());

        //when
        String token = jwtService.createRefreshToken(payload);

        //then
        Assertions.assertNotNull(token);
    }
//    @Test
//    @DisplayName("토큰 검증 성공")
//    void 토큰검증_성공() throws Exception {
//        //given
//        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID(), Role.USER, new Date());
//        String token = jwtService.createAccessToken(payload);
//
//        //when
//        Claims claims = jwtService.verifyToken(token);
//        //then
//        Assertions.assertEquals(payload.id().toString(), claims.getSubject());
//    }



}
