package com.pet.pet_funeral.domain.user.service.impl;

import com.pet.pet_funeral.domain.user.model.LoginType;
import com.pet.pet_funeral.domain.user.model.User;
import com.pet.pet_funeral.security.dto.LoginResponse;

public interface SocialLoginService {
    LoginResponse login(String code);

    LoginType getLoginType();

    User register(String socialId);
}
