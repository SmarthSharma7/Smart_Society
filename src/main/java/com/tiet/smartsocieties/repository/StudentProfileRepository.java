package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
}
