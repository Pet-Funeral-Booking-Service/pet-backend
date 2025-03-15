package com.pet.pet_funeral.security.dto;



import com.pet.pet_funeral.domain.user.model.Role;

import java.util.Date;
import java.util.UUID;

public record AccessTokenPayload(UUID id, Role role, Date date) {

    // 토큰에 담길 정보들을 담아놓는 클래스 (payload)
    // 사용자 정보,역할, 토큰 생성 일시
}
