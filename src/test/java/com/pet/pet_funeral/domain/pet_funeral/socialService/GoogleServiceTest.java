//package com.pet.pet_funeral.domain.pet_funeral.socialService;
//
//import com.pet.pet_funeral.domain.user.model.LoginType;
//import com.pet.pet_funeral.domain.user.model.Role;
//import com.pet.pet_funeral.domain.user.model.User;
//import com.pet.pet_funeral.domain.user.repository.UserRepository;
//import com.pet.pet_funeral.domain.user.service.GoogleService;
//import com.pet.pet_funeral.domain.user.service.RefreshTokenService;
//import com.pet.pet_funeral.security.dto.AccessTokenPayload;
//import com.pet.pet_funeral.security.dto.LoginResponse;
//import com.pet.pet_funeral.security.dto.RefreshTokenPayload;
//import com.pet.pet_funeral.security.jwt.JwtService;
//import com.pet.pet_funeral.security.service.CookieService;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import okhttp3.mockwebserver.RecordedRequest;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseCookie;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.Optional;
//import java.util.UUID;
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@TestPropertySource(properties = {
//        "google.client_id=test-client-id",
//        "google.client_secret=test-secret",
//        "google.redirect_uri=http://localhost/callback",
//        "google.user_url=http://localhost:8081/user",
//        "google.token_url=http://localhost:8081/token"
//})
//public class GoogleServiceTest {
//    @Autowired
//    private GoogleService googleService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @MockitoBean
//    private JwtService jwtService;
//
//    @Mock
//    private CookieService cookieService;
//
//    @Mock
//    private RefreshTokenService refreshTokenService;
//
//    private static MockWebServer mockWebServer;
//
//    @BeforeAll
//    static void setupMockServer() throws IOException {
//        mockWebServer = new MockWebServer();
//        mockWebServer.start(8081);
//    }
//
//    @AfterAll
//    static void shutDownMockServer() throws IOException {
//        mockWebServer.shutdown();
//    }
//
//    @Test
//    @DisplayName("구글 로그인 성공~")
//    void 구글_로그인_성공() {
//        // given
//        String code = "mock-auth-code";
//        String socialToken = "mock-social-token";
//        String socialId = "mock-social-id";
//        UUID userId = UUID.randomUUID();
//
//        User user = User.builder()
//                .id(userId)
//                .socialId(socialId)
//                .role(Role.USER)
//                .loginType(LoginType.GOOGLE)
//                .build();
//
//        // 토큰 요청
//        String tokenJson = """
//                {
//                "token_type": "Bearer",
//                "access_token" : "%s",
//                "expires_in" : 1000
//                }
//                """.formatted(socialToken);
//
//        mockWebServer.enqueue(new MockResponse()
//                .setBody(tokenJson)
//                .addHeader("Content-Type", "application/json")
//                .setResponseCode(200));
//
//        // 사용자 조회 요청
//        String userJson = """
//                {
//                "id" : "%s",
//                "email" : "이메일인데용",
//                "verified_email" : false,
//                "name" : "최영민",
//                "locale" : "en-US"
//                }
//                """.formatted(socialId);
//
//        mockWebServer.enqueue(new MockResponse()
//                .setBody(userJson)
//                .addHeader("Content-Type", "application/json")
//                .setResponseCode(200));
//
//        String accessToken = "mock-access-token";
//        String refreshToken = "mock-refresh-token";
//        ResponseCookie responseCookie = ResponseCookie.from("refreshToken",refreshToken).build();
//
//
//        Mockito.when(userRepository.findBySocialId(socialId)).thenReturn(Optional.of(user));
//
//        Mockito.when(jwtService.createAccessToken(Mockito.any())).thenReturn(accessToken);
//        Mockito.when(jwtService.createRefreshToken(Mockito.any())).thenReturn(refreshToken);
//
//        Mockito.when(cookieService.createRefreshTokenCookie(refreshToken)).thenReturn(responseCookie);
//
//        Mockito.doNothing().when(refreshTokenService).updateRefreshToken(userId, refreshToken);
//
//
//        // when
//        LoginResponse loginResponse = googleService.login(code);
//
//        // then
//        Assertions.assertThat(loginResponse.getAccessToken()).isEqualTo(accessToken);
//        Assertions.assertThat(loginResponse.getRefreshTokenCookie().getValue()).isEqualTo(refreshToken);
//    }
//}

