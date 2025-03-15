package com.pet.pet_funeral.domain.pet_funeral.service;

import com.pet.pet_funeral.domain.pet_funeral.model.PetFuneral;
import com.pet.pet_funeral.domain.pet_funeral.repository.PetFuneralRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetFuneralService {
    private final PetFuneralRepository petFuneralRepository;

    @Transactional
    public UUID save(PetFuneral petFuneral) {
        PetFuneral save = petFuneralRepository.save(petFuneral);
        return save.getId();
    }

    public Page<PetFuneral> findByCity(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return petFuneralRepository.findByAddress_City(city, pageable);
    }
}
