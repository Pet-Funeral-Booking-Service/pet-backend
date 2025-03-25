package com.pet.pet_funeral.domain.pet_funeral.service;


import com.pet.pet_funeral.domain.user.model.LoginType;
import com.pet.pet_funeral.domain.user.model.RefreshToken;
import com.pet.pet_funeral.domain.user.model.Role;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.RefreshTokenRepository;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.domain.user.service.RefreshTokenService;
import com.pet.pet_funeral.infra.redis.service.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.BDDMockito.*;



@ExtendWith(MockitoExtension.class) // 단위테스트용
public class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    private final String newRefreshToken = "refreshToken";
    private final String socialId = "socialId1";
    private final UUID userId = UUID.randomUUID();
    private final UUID refreshTokenId = UUID.randomUUID();
    private long refreshKeyExpiration = 10000L;


    @Test
    @DisplayName("리프레시토큰이 이미 존재할때 업데이트 테스트")
    void 리프레시토큰_DB에존재할때() throws Exception {
        //given
       User user = createUser();

        RefreshToken refreshToken = RefreshToken.builder()
                .id(refreshTokenId)
                .token("토큰인데용")
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusSeconds(refreshKeyExpiration))
                .build();

        BDDMockito.given(userRepository.findById(userId)).willReturn(Optional.of(user));
        BDDMockito.given(refreshTokenRepository.findByUser(user)).willReturn(Optional.ofNullable(refreshToken));

        //when
        refreshTokenService.updateRefreshToken(userId,newRefreshToken);

        // then
        verify(userRepository).findById(userId);
        verify(refreshTokenRepository).findByUser(user);
        verify(redisService).updateRefreshToken(userId,newRefreshToken);

        Assertions.assertEquals(newRefreshToken,refreshToken.getToken());
    }
    @Test
    @DisplayName("리프레시토큰이 존재하지 않을 때 새로운 리프레시토큰 저장")
    void 리프레시토큰_DB에존재안할때() throws Exception {
        //given
        User user = createUser();

        BDDMockito.given(userRepository.findById(userId)).willReturn(Optional.of(user));
        BDDMockito.given(refreshTokenRepository.findByUser(user)).willReturn(Optional.empty()); // 리프레시토큰값이 없을 때

        RefreshToken refreshToken = RefreshToken.builder()
                .id(refreshTokenId)
                .token(newRefreshToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusSeconds(refreshKeyExpiration))
                .build();

        BDDMockito.given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);
        //when
        refreshTokenService.updateRefreshToken(userId,refreshToken.getToken());

        //then
        verify(userRepository).findById(userId);
        verify(refreshTokenRepository).findByUser(user);
        verify(redisService).updateRefreshToken(userId,refreshToken.getToken());

        // 새로 만든 리프레시토큰값이 일치한지 비교
        Assertions.assertEquals(newRefreshToken,refreshToken.getToken());
    }
    
    // 기존에 존재하던 유저를 만드는 용도
    private User createUser() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .socialId(socialId)
                .loginType(LoginType.KAKAO)
                .role(Role.USER)
                .build();
        return user;
    }

    // 1. 리프레시토큰이 이미 존재해서 업데이트만 하는 경우
    // 2. 리프레시토큰이 존재하지 않아 새로 저장 후 업데이트 하는 경우
    // 3. 예외) 해당 user 가 존재 x

    /**
     * 리프레시토큰을 string 타입으로 비교안하고 객체로 해야되는 이유
     * 엔티티 상태를 테스트하는 것이므로 객체 형태로 비교해야함
     */

    /**
     * verify?
     * 특정 메서드가 호출됐는지, 호출됐다면 몇번 호출됐는지 검증하는데 사용
     * 메서드 실행 과정과 결과 검증
     * 실제 데이터의 반영은 아니고 메서드가 호출됐는지만 검증임
     */
}
/**
 *  독립적,반복 실행 가능하게
 *  db,파일에 의존하는 테스트는 격리하거나 MOCK 처
 *  메서드 이름, 테스트 이름 직관적이게
 *
 */

/**
 * any 를 쓰는 이유?
 * 새로운 인스턴스를 생성해서 쓸 때는 any 를 써서 어떤 해당 객체든 상관없이 해당 객체를 반환해준다.
 */