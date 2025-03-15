package com.pet.pet_funeral.domain.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class RefreshToken {

    @Id
    @UuidGenerator
    @Column(name = "refresh_token_id",nullable = false,updatable = false)
    private UUID id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User user;

    @Column(name ="token",nullable = false,updatable = true)
    private String token;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    public RefreshToken(User user, String token, LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.user = user;
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }
}
