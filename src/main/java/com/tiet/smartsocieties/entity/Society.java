package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.*;

@Entity
@Table(name = "societies")
@Getter
@Setter
@NoArgsConstructor
public class Society {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "society_id")
    Long societyId;

    String name;

    String description;

    @Column(name = "contact_email")
    String contactEmail;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "society_status")
    SocietyStatus status;

    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    LocalDateTime updatedAt = LocalDateTime.now();

    public enum SocietyStatus {
        ACTIVE, INACTIVE
    }
}