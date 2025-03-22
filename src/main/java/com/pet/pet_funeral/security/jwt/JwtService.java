package com.pet.pet_funeral.security.jwt;

import com.pet.pet_funeral.exception.code.JwtExpirationExceptionCode;
import com.pet.pet_funeral.exception.code.JwtVerifyExceptionCode;
import com.pet.pet_funeral.security.dto.AccessTokenPayload;
import com.pet.pet_funeral.security.dto.LoginResponse;
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

//hs512 알고리즘 사용
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


    public JwtService(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public Claims verifyToken(String token){
        try {
            log.info("Verifying token {}", token);
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (IllegalArgumentException e){
            throw new JwtVerifyExceptionCode("JWT token is invalid");
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = verifyToken(token);
            return claims.getExpiration().before(new Date());
        }catch (JwtVerifyExceptionCode e){
            throw new JwtExpirationExceptionCode("JWT token is expired");
        }
    }

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

    public String extractAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token =  authorizationHeader.substring(7); // "Bearer " 이후부터 잘라서 토큰만 리턴
            log.info("추출된 엑세스 토큰 : " + token);
            return token;
        }
        return null;
    }
}
