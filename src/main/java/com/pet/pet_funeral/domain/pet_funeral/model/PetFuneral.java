package com.pet.pet_funeral.domain.pet_funeral.model;

import com.pet.pet_funeral.domain.address.model.Address;
import com.pet.pet_funeral.domain.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`pet_funeral`")
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
    private String phoneNumber;

    @Column(name = "homepage", nullable = true)
    private String homepage;

    @Column(name = "legal", nullable = true)
    private boolean legal;

    @Setter
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
