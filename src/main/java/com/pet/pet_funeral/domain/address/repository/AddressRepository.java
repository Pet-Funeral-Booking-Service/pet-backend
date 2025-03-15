package com.pet.pet_funeral.domain.address.repository;

import com.pet.pet_funeral.domain.address.model.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
}
