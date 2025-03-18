package com.pet.pet_funeral.security.jwt;

import com.pet.pet_funeral.security.dto.AccessTokenPayload;
import com.pet.pet_funeral.security.dto.RefreshTokenPayload;
import com.pet.pet_funeral.security.service.CookieService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

//hs256 알고리즘 사용
@Slf4j
@Service
public class JwtService {

    private final SecretKey secretKey;

    @Value("${spring.application.name}")
    private String issuer; // 발급자 정보 식별위함

    @Value("${jwt.access.expiration}")
    private long accessKeyExpiration; // 30분

    @Value("${jwt.refresh.expiration}")
    private long refreshKeyExpiration; // 14일

    @Value("${jwt.access.header}")
    private String accessHeader;

    private final CookieService cookieService;

    public JwtService(@Value("${jwt.secret-key}") String secretKey, CookieService cookieService) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.cookieService = cookieService;
    } // 시크릿 키를 객체로 사용해서 바로 사용 가능, 보안도 굿

    /**
     * 토큰 유효성 검증이후 Claims 반환
     */
    public Claims verifyToken(String token) throws ServletException, IOException {
        try {
            log.info("Verifying token {}", token);
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e) {
            log.error("JWT token verification failed", e);
            throw e;

        }
    }
    /**
     * 토큰의 만료 여부만 판단
     * 토큰 재발급 시 사용
     */
    public boolean isTokenExpired(String token) {
        try{
            Claims claims = verifyToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.error(e.getMessage());
            return true;
        }
    }

    /**
     * 토큰 생성
     */
    public String createAccessToken(AccessTokenPayload payload){
        return Jwts.builder()
                .subject(String.valueOf(payload.id()))
                .claim("role",payload.role().name())
                .issuer(issuer)
                .issuedAt(payload.date())
                .expiration(new Date(payload.date().getTime() + accessKeyExpiration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(RefreshTokenPayload payload){
        return Jwts.builder()
                .subject(String.valueOf(payload.id()))
                .issuer(issuer)
                .issuedAt(payload.date())
                .expiration(new Date(payload.date().getTime() + refreshKeyExpiration))
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }

    /**
     * 엑세스,리프레시 토큰 헤더 전송
     */
    public void sendAccessTokenAndRefreshCookie(HttpServletResponse response,String accessToken,String refreshToken){
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response,accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE,cookieService.createRefreshTokenCookie(refreshToken).toString());
        log.info("Access Token,Refresh Token 설정 완료");
    }

    public void setAccessTokenHeader(HttpServletResponse response,String accessToken){
        response.setHeader(accessHeader,"Bearer " + accessToken.trim());
    }

    // AccessToken 추출
    public String extractAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token =  authorizationHeader.substring(7); // "Bearer " 이후부터 잘라서 토큰만 리턴
            log.info("추출된 엑세스 토큰 : " + token);
            return token;
        }

        return null;  // 토큰이 없으면 null 리턴
    }





}
