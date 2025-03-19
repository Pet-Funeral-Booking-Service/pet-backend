package com.pet.pet_funeral.domain.pet_funeral.service;

import com.pet.pet_funeral.domain.address.model.Address;
import com.pet.pet_funeral.domain.address.repository.AddressRepository;
import com.pet.pet_funeral.domain.pet_funeral.dto.PetFuneralRequest;
import com.pet.pet_funeral.domain.pet_funeral.mapper.PetFuneralMapper;
import com.pet.pet_funeral.domain.pet_funeral.model.PetFuneral;
import com.pet.pet_funeral.domain.pet_funeral.repository.PetFuneralRepository;
import com.pet.pet_funeral.exception.code.BadRequestExceptionCode;
import com.pet.pet_funeral.exception.code.ExistValueExceptionCode;
import com.pet.pet_funeral.exception.code.NotFoundDataExceptionCode;
import com.pet.pet_funeral.utils.OptionalUtil;
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
    private final AddressRepository addressRepository;
    private OptionalUtil optionalUtil;

    @Transactional
    public UUID save(PetFuneralRequest petFuneralDto) {
        boolean exists = petFuneralRepository.existsByName(petFuneralDto.name());
        if (exists) {
            throw new ExistValueExceptionCode("이미 존재하는 장례식장 이름입니다.");
        }
        Address address = addressRepository.save(petFuneralDto.address());
        PetFuneral petFuneral = petFuneralMapper.toMessageBodyDto(petFuneralDto);
        petFuneral.setAddress(address);
        return petFuneralRepository.save(petFuneral).getId();
    }

    public PetFuneral findById(UUID id) {
        return OptionalUtil.getOrElseThrow(petFuneralRepository.findById(id),
                "해당 장례식장이 존재하지 않습니다.");
    }

    public Page<PetFuneral> findByCity(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return petFuneralRepository.findByAddress_City(city, pageable);
    }
}
