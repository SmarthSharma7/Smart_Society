package com.tiet.smartsocieties.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EventRequest(
        @NotNull Long societyId,
        @NotNull Long categoryId,
        @NotBlank String title,
        String description,
        String venue,
        @NotNull LocalDateTime startDatetime,
        @NotNull LocalDateTime endDatetime,
        LocalDateTime registrationDeadline,
        Integer maxCapacity,
        String status
) {}
