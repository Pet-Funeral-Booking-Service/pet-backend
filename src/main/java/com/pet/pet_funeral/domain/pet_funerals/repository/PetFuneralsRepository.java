package com.pet.pet_funeral.domain.pet_funerals.repository;

import com.pet.pet_funeral.domain.pet_funerals.model.PetFunerals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetFuneralsRepository extends JpaRepository<PetFunerals, UUID> {
}
