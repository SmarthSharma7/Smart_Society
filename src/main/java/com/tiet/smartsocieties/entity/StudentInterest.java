package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_interests")
@Getter
@Setter
@NoArgsConstructor
public class StudentInterest {
    @EmbeddedId
    StudentInterestId id;
    Double weight = 1.0;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentInterestId {
        @Column(name = "student_id")
        Long studentId;
        @Column(name = "tag_id")
        Long tagId;
    }
}
