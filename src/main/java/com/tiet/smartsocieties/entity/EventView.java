package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "event_views")
@Getter
@Setter
@NoArgsConstructor
public class EventView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    Long viewId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    User student;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    @Column(name = "viewed_at")
    LocalDateTime viewedAt = LocalDateTime.now();
    String source;
}
