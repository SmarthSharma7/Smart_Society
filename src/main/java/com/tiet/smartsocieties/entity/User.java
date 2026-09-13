package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;
    String name;
    @Column(unique = true)
    String email;
    @Column(name = "password_hash")
    String passwordHash;
    @Enumerated(EnumType.STRING)
    Enums.UserRole role;
    @Column(name = "is_active")
    boolean active = true;
    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    LocalDateTime updatedAt = LocalDateTime.now();
}
