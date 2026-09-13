package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.Feedback;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByEventSocietySocietyId(Long societyId);

    Optional<Feedback> findByEventEventIdAndStudentUserId(Long eventId, Long studentId);

    List<Feedback> findByEventEventId(Long eventId);
}
