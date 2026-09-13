package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.Event;
import org.springframework.data.jpa.repository.*;

import java.time.*;
import java.util.*;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatusAndStartDatetimeAfterOrderByStartDatetimeAsc(Event.EventStatus status, LocalDateTime now);

    List<Event> findByStatusAndTitleContainingIgnoreCaseOrderByStartDatetimeAsc(Event.EventStatus status, String q);

    List<Event> findBySocietySocietyIdOrderByStartDatetimeDesc(Long societyId);
}
