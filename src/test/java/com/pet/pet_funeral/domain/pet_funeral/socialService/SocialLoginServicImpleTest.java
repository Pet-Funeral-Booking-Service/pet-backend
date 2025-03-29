package com.pet.pet_funeral.domain.pet_funeral.socialService;

import com.pet.pet_funeral.domain.user.model.LoginType;
import com.pet.pet_funeral.domain.user.model.Role;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.domain.user.service.GoogleService;
import com.pet.pet_funeral.domain.user.service.RefreshTokenService;
import com.pet.pet_funeral.domain.user.service.impl.SocialLoginService;
import com.pet.pet_funeral.domain.user.service.impl.SocialLoginServiceImpl;
import com.pet.pet_funeral.security.dto.AccessTokenPayload;
import com.pet.pet_funeral.security.dto.LoginResponse;
import com.pet.pet_funeral.security.dto.RefreshTokenPayload;
import com.pet.pet_funeral.security.jwt.JwtService;
import com.pet.pet_funeral.security.service.CookieService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SocialLoginServicImpleTest {

    private SocialLoginServiceImpl socialLoginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoogleService googleService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CookieService cookieService;

    @Mock
    private RefreshTokenService refreshTokenService;



    @Test
    @DisplayName("사용자가 DB에 있을 때 로그인 성공")
    void 사용자가_DB에_있을때_로그인_성공 () throws Exception {
        //given
        String code = "asdf";
        String socialId = "aaaaaaa";

        User user = User.builder()
                .id(UUID.randomUUID())
                .socialId(socialId)
                .role(Role.USER)
                .loginType(LoginType.KAKAO)
                .build();

        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";

        ResponseCookie responseCookie = cookieService.createRefreshTokenCookie(refreshToken);

        Mockito.when(googleService.getToken(code)).thenReturn("mockToken");
        Mockito.when(googleService.getUser("mockToken")).thenReturn(socialId);
        Mockito.when(userRepository.findBySocialId(socialId)).thenReturn(Optional.ofNullable(user));
        Mockito.when(jwtService.createAccessToken(Mockito.any())).thenReturn(accessToken);
        Mockito.when(jwtService.createRefreshToken(Mockito.any())).thenReturn(refreshToken);
        Mockito.when(cookieService.createRefreshTokenCookie(refreshToken)).thenReturn(responseCookie);
        Mockito.doNothing().when(refreshTokenService).updateRefreshToken(user.getId(),refreshToken);

        //when
        LoginResponse loginResponse = socialLoginService.login(code);
        //then
        Assertions.assertThat(loginResponse.getAccessToken()).isEqualTo(accessToken);
        Assertions.assertThat(loginResponse.getRefreshTokenCookie()).isEqualTo(responseCookie);

    }

}
