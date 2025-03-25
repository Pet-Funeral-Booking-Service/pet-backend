package com.pet.pet_funeral.domain.pet_funeral.service;

import com.pet.pet_funeral.domain.user.repository.UserRepository;
import com.pet.pet_funeral.domain.user.service.GoogleService;
import com.pet.pet_funeral.domain.user.service.RefreshTokenService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GoogleServiceTest {

    @InjectMocks
    private GoogleService googleService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserRepository userRepository;
}
