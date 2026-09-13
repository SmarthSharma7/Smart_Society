package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
}
