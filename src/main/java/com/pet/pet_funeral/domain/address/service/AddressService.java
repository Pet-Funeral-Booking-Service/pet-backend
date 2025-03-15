package com.pet.pet_funeral.domain.address.service;

import com.pet.pet_funeral.domain.address.model.Address;
import com.pet.pet_funeral.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public void save(Address address) {
        addressRepository.save(address);
    }
}
