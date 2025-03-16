package com.pet.pet_funeral.domain.pet_funeral.dto;

import com.pet.pet_funeral.domain.address.model.Address;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record PetFuneralRequest(
        @NotNull String name,
        LocalTime openAt,
        LocalTime closeAt,
        Integer price,
        String phoneNumber,
        String homepage,
        boolean legal,
        Address address) {

}
