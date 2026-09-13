package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @Enumerated(EnumType.STRING)
    SocietyStatus status = SocietyStatus.ACTIVE;
    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    LocalDateTime updatedAt = LocalDateTime.now();

    public enum SocietyStatus {ACTIVE, INACTIVE}
}
