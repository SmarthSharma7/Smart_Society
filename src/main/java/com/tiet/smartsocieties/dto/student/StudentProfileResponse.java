package com.tiet.smartsocieties.dto.student;

public record StudentProfileResponse(
        Long studentId,
        String rollNumber,
        String branch,
        Integer year,
        String section
) {
}