package com.pet.pet_funeral.security.dto;

import java.util.Date;
import java.util.UUID;

public record RefreshTokenPayload(UUID id, Date date) {
}
