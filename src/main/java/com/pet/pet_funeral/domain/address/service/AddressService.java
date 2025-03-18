package com.pet.pet_funeral.domain.address.service;

import com.pet.pet_funeral.domain.address.model.Address;
import com.pet.pet_funeral.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
    private final AddressRepository addressRepository;

    @Transactional
    public void save(Address address) {
        addressRepository.save(address);
    }
}
