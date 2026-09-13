package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    Long recommendationId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    User student;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    Double score;
    String reason;
    @Column(name = "generated_at")
    LocalDateTime generatedAt = LocalDateTime.now();
    boolean viewed;
    boolean clicked;
}
