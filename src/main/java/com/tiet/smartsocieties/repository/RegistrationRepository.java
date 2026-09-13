package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.Registration;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByEventEventIdAndStudentUserId(Long eventId, Long studentId);

    List<Registration> findByStudentUserId(Long studentId);

    long countByEventEventIdAndStatus(Long eventId, Registration.RegistrationStatus status);

    List<Registration> findByEventSocietySocietyId(Long societyId);
}
