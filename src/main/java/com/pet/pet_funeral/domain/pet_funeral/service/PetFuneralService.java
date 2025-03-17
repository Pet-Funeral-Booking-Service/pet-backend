package com.pet.pet_funeral.domain.pet_funeral.service;

import com.pet.pet_funeral.domain.pet_funeral.dto.PetFuneralRequest;
import com.pet.pet_funeral.domain.pet_funeral.mapper.PetFuneralMapper;
import com.pet.pet_funeral.domain.pet_funeral.model.PetFuneral;
import com.pet.pet_funeral.domain.pet_funeral.repository.PetFuneralRepository;
import com.pet.pet_funeral.exception.code.BadRequestExceptionCode;
import com.pet.pet_funeral.exception.code.ExistValueExceptionCode;
import com.pet.pet_funeral.exception.code.NotFoundDataExceptionCode;
import java.util.UUID;
import java.util.function.Supplier;
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
    private final PetFuneralMapper petFuneralMapper;

    @Transactional
    public UUID save(PetFuneralRequest petFuneralDto) {
        boolean exists = petFuneralRepository.existsByName(petFuneralDto.name());
        if (exists) {
            throw new ExistValueExceptionCode("이미 존재하는 장례식장 이름입니다.");
        }
        PetFuneral petFuneral = petFuneralMapper.toMessageBodyDto(petFuneralDto);
        return petFuneralRepository.save(petFuneral).getId();
    }

    public PetFuneral findById(UUID id) {
        return petFuneralRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataExceptionCode("해당 장례식장이 존재하지 않습니다."));
    }

    public Page<PetFuneral> findByCity(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return petFuneralRepository.findByAddress_City(city, pageable);
    }
}
