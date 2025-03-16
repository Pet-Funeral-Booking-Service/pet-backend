package com.pet.pet_funeral.security.jwt;

import com.pet.pet_funeral.domain.user.model.RefreshToken;
import com.pet.pet_funeral.domain.user.repository.RefreshTokenRepository;
import com.pet.pet_funeral.domain.user.service.RefreshTokenService;
import com.pet.pet_funeral.infra.redis.service.RedisService;
import com.pet.pet_funeral.security.dto.LoginResponse;
import com.pet.pet_funeral.security.service.CookieService;
import com.pet.pet_funeral.security.service.LoginService;
import com.pet.pet_funeral.utils.OptionalUtil;
import com.pet.pet_funeral.utils.SuccessResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RedisService redisService;
    private final LoginService loginService;
    private final CookieService cookieService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인 후 사용자가 api 요청할 때 사용되는 필터임
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = String.valueOf(jwtService.extractAccessToken(request));
            log.info("Access token: {}", accessToken);

            if(accessToken != null && !jwtService.isTokenExpired(accessToken)){
                checkAccessToken(accessToken);
                filterChain.doFilter(request, response);
                return;
            }
            validateRefreshToken(request,response);

            String newAccessToken = String.valueOf(jwtService.extractAccessToken(request));
            checkAccessToken(newAccessToken);
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

    /**
     * 토큰 검증 후 filter
     */
    private ResponseEntity<SuccessResponse> validateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        // 쿠키에서 리프레시 토큰 가져오기
        Optional<String> refreshTokenOpt = cookieService.getRefreshTokenFromCookie(request);
        // 토큰 존재 유무 확인
        if(refreshTokenOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new SuccessResponse(false, "RefreshToken 이 없습니다.", null));
        }
        // 토큰 유효성 검증
        String refreshToken = refreshTokenOpt.get(); // 사용자의 리프레시 토큰
        jwtService.isTokenExpired(refreshToken); // 예외처리 추후에 설정

        Optional<UUID> userId = redisService.getUserIdByRefreshToken(refreshToken);

        String redisToken = String.valueOf(redisService.getRefreshToken(userId.get()));

        if (redisToken == null || !redisToken.equals(refreshToken)) {
            RefreshToken refreshTokenInDB = OptionalUtil.getOrElseThrow(refreshTokenRepository.findByToken(refreshToken),"존재하지 않는 refreshToken 입니다.");

            if(!refreshTokenInDB.getToken().equals(refreshToken)){
                throw new IllegalArgumentException("Refresh Token Not Matched");
            }
            redisService.saveRefreshToken(userId.get(), refreshToken);
        }

        // 엑세스 토큰 생성
        LoginResponse loginResponse = loginService.login(userId.get());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer " + loginResponse.getAccessToken());
        headers.add(HttpHeaders.SET_COOKIE,loginResponse.getRefreshTokenCookie().toString());

        return ResponseEntity.ok().headers(headers)
                .body(new SuccessResponse(true, loginResponse.getAccessToken(), loginResponse.getRefreshTokenCookie()));

    }


    private void checkAccessToken(String accessToken) throws ServletException, IOException {
        Claims claims = jwtService.verifyToken(accessToken);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(grantedAuthority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("요청 경로: {}", path);
        return path.startsWith("/v1/api/kakao") || path.equals("/favicon.ico") || path.startsWith("/v1/api/google");

    }
/**
 * favicon  은 탭에 아이콘을 브라우저에서 자동으로 요청한다.
 * 그래서 favicon 은 토큰이 없는 경로이므로 필터에서 제외시켜야한다.
 */

}
