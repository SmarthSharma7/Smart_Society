package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "event_registrations", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "student_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    Long registrationId;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    @ManyToOne
    @JoinColumn(name = "student_id")
    User student;
    @Enumerated(EnumType.STRING)
    RegistrationStatus status;
    @Column(name = "registered_at")
    LocalDateTime registeredAt = LocalDateTime.now();
    @Column(name = "cancelled_at")
    LocalDateTime cancelledAt;

    public enum RegistrationStatus {INTERESTED, REGISTERED, CANCELLED, ATTENDED, NO_SHOW}
}
