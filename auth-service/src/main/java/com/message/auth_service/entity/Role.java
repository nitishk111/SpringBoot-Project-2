package com.message.auth_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.util.List;

@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"roleId","role"})
})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    @Column(nullable = false, length = 15)
    private String role;
}
