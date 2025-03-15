package com.pet.pet_funeral.domain.pet_funeral.model;

import com.pet.pet_funeral.domain.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`pet_funerals`")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class PetFuneral extends BaseTime {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "open_at", nullable = true)
    private LocalTime openAt;


    @Column(name = "close_at", nullable = true)
    private LocalTime closeAt;

    @Column(name = "price", nullable = true)
    private Integer price;

    @Column(name = "phone_number", nullable = true)
    private Integer phoneNumber;

    @Column(name = "homepage", nullable = true)
    private String homepage;

    @Column(name = "is_legal", nullable = true)
    private boolean isLegal;
}
