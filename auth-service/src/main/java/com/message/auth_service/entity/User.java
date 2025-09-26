package com.message.auth_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * created By : Nitish
 */

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userName"}),
        @UniqueConstraint(columnNames = {"userEmail"})
})
@SequenceGenerator(name = "user_id_generator", initialValue = 111, allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    private long userId;

    @Column(nullable = false, length = 10)
    private String userName;

    @Column(nullable = false, length = 20)
    private String userEmail;

    @Column(nullable = false, length = 127)
    private String userPassword;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="roleId", nullable = false)
    private Role role = new Role(2);

    @Column(nullable = false)
    private boolean isActive;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isActive=true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
