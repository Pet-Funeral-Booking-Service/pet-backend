package com.pet.pet_funeral.api;

import com.pet.pet_funeral.domain.user.service.KakaoService;
import com.pet.pet_funeral.security.dto.LoginResponse;
import com.pet.pet_funeral.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login")
    public ResponseEntity<?> callback(@RequestParam String code){
        LoginResponse loginResponse = kakaoService.kakaoLogin(code);
        SuccessResponse response = new SuccessResponse(true,"카카오 로그인 성공",loginResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
//http://localhost:8080/v1/api/kakao/login
//https://kauth.kakao.com/oauth/authorize -> 인가코드 받는 url(프론트에서 처리)
//https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=3c78ccc2c22f2c064f9e28457bcdb0c9&redirect_uri=http://localhost:8080/v1/api/kakao/login
// 위 경로에서 받은 인가코드로 파라미터를 code 로 해서 이후에 로그인 처리