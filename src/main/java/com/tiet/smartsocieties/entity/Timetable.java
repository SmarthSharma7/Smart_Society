package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "timetable")
@Getter
@Setter
@NoArgsConstructor
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    Long timetableId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    User student;
    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    Day day;
    @Column(name = "start_time")
    LocalTime startTime;
    @Column(name = "end_time")
    LocalTime endTime;
    @Column(name = "course_code")
    String courseCode;
    @Column(name = "course_name")
    String courseName;
    String room;

    public enum Day {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY}
}
