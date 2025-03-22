package com.pet.pet_funeral.security.dto;



import com.pet.pet_funeral.domain.user.model.Role;

import java.util.Date;
import java.util.UUID;

public record AccessTokenPayload(UUID id, Role role, Date date) {
}


