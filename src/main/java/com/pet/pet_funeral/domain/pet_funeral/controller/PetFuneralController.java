package com.pet.pet_funeral.domain.pet_funeral.controller;

import com.pet.pet_funeral.domain.pet_funeral.dto.PetFuneralRequest;
import com.pet.pet_funeral.domain.pet_funeral.service.PetFuneralService;
import com.pet.pet_funeral.utils.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/petFuneral")
public class PetFuneralController {
    private final PetFuneralService petFuneralService;

    @PostMapping("/register")
    public ResponseEntity<?> createPetFuneral(@RequestBody @Valid PetFuneralRequest request) {
        petFuneralService.save(request);
        SuccessResponse<String> response = new SuccessResponse<>(true, "장례식장 등록 성공", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
