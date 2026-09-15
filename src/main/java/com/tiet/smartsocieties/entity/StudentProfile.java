package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
public class StudentProfile {
    @Id
    @Column(name = "student_id")
    Long studentId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    User user;
    @Column(name = "roll_number")
    String rollNumber;
    String branch;
    Integer year;
    String section;
}
