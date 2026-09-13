package com.tiet.smartsocieties.dto.event;

import java.time.LocalDateTime;

public record EventResponse(
        Long eventId,
        Long societyId,
        String societyName,
        Long categoryId,
        String categoryName,
        String title,
        String description,
        String venue,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        LocalDateTime registrationDeadline,
        Integer maxCapacity,
        String status
) {}
