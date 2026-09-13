package com.tiet.smartsocieties.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interest_tags")
@Getter
@Setter
@NoArgsConstructor
public class InterestTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    Long tagId;
    String name;
}
