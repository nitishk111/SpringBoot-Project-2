package com.message.auth_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Data
@Entity
public class PasswordOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private int otp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean flag;

    @PrePersist
    public void prePersist() {
        expiresAt = LocalDateTime.now().plusMinutes(10);
        flag = true;
        int bound = (int) Math.pow(10, 4);
        otp = new SecureRandom().nextInt(bound - bound/10) + bound/10;
    }

}
