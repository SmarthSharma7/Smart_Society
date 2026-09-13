package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long eventId;
    @ManyToOne
    @JoinColumn(name = "society_id")
    Society society;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    String title;
    String description;
    String venue;
    @Column(name = "start_datetime")
    LocalDateTime startDatetime;
    @Column(name = "end_datetime")
    LocalDateTime endDatetime;
    @Column(name = "registration_deadline")
    LocalDateTime registrationDeadline;
    @Column(name = "max_capacity")
    Integer maxCapacity;
    @Enumerated(EnumType.STRING)
    EventStatus status = EventStatus.PENDING;
    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    LocalDateTime updatedAt = LocalDateTime.now();

    public enum EventStatus {DRAFT, PENDING, APPROVED, REJECTED, CANCELLED, COMPLETED}
}
