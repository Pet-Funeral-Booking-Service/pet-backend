package com.pet.pet_funeral.domain.pet_funeral.controller;

import com.pet.pet_funeral.domain.pet_funeral.service.PetFuneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PetFuneralController {
    private final PetFuneralService petFuneralService;
}
