package com.pet.pet_funeral.domain.user.repository;

import com.pet.pet_funeral.domain.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokensRepository extends JpaRepository<Users, UUID> {
}
