package com.pet.pet_funeral.domain.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`refresh_tokens`")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class RefreshTokens {

    @Id
    @Column(name = "token", nullable = false, updatable = true)
    private String token;
}
